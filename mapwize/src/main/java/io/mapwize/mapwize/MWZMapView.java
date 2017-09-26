package io.mapwize.mapwize;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class MWZMapView extends WebView implements LocationListener, BeaconConsumer, SensorEventListener {

    final private String SERVER_URL = "https://www.mapwize.io";
    final private String ANDROID_SDK_VERSION = "2.3.4";
    final private String ANDROID_SDK_NAME = "ANDROID SDK";
    private static String CLIENT_APP_NAME;
    private boolean isLoaded = false;
    private MWZMapViewListener listener;
    private MWZMapOptions options;
    private Integer floor;
    private Integer[] floors;
    private Integer zoom;
    private boolean followUserMode;
    private MWZCoordinate center;
    private MWZUserPosition userPosition;
    private HashMap<String, Object> callbackMemory = new HashMap<>();
    private HashMap<String,String> universesByVenues = new HashMap<>();

    private LocationManager locationManager;
    private BeaconManager beaconManager;
    private BackgroundPowerSaver backgroundPowerSaver;

    private Region[] monitoredRegions;
    private float currentDegree = 0f;
    private SensorManager mSensorManager;


    private String initString = "map.on('zoomend', function(e){android.onZoomEnd((function(){return map.getZoom()})())});" +
            "map.on('click', function(e){android.onClick((function(){return JSON.stringify(e.latlng)})())});" +
            "map.on('contextmenu', function(e){android.onContextMenu((function(){return JSON.stringify(e.latlng)})())});" +
            "map.on('floorChange', function(e){android.onFloorChange((function(){return map._floor})())});" +
            "map.on('floorsChange', function(e){android.onFloorsChange((function(){return JSON.stringify(map._floors)})())});" +
            "map.on('placeClick', function(e){android.onPlaceClick((function(){return JSON.stringify(e.place)})())});" +
            "map.on('venueClick', function(e){android.onVenueClick((function(){return JSON.stringify(e.venue)})())});" +
            "map.on('markerClick', function(e){android.onMarkerClick((function(){return JSON.stringify({lat:e.latlng.lat, lon:e.latlng.lng, floor:e.floor})})())});" +
            "map.on('moveend', function(e){android.onMoveEnd((function(){return JSON.stringify(map.getCenter())})())});" +
            "Mapwize.Location.on('userPositionChange', function(e){android.onUserPositionChange((function(){return JSON.stringify(e.userPosition)})())});" +
            "map.on('followUserModeChange', function(e){android.onFollowUserModeChange((function(){return JSON.stringify(e.active)})())});" +
            "map.on('directionsStart', function(e){android.onDirectionsStart((function(){return 'Directions loaded'})())});" +
            "map.on('directionsStop', function(e){android.onDirectionsStop((function(){return 'Directions stopped'})())});" +
            "map.on('apiResponse', function(e){android.onLoadUrlResponse((function(){return JSON.stringify({type:e.type, returnedType: e.returnedType, hash:e.hash, response:e.response, error:e.error})})())});" +
            "map.on('universeChange', function(e){android.onUniverseChange((function(){return JSON.stringify({venueId:e.venueId, universeId:e.universeId});})())});" +
            "Mapwize.Location.setUseBrowserLocation(false);";


    // This constructor should not be used
    public MWZMapView(Context context) {
        super(context);
        CLIENT_APP_NAME = context.getPackageName();
        this.options = new MWZMapOptions();
        this.initialize();
    }

    public MWZMapView(Context context, MWZMapOptions opts) {
        super(context);
        CLIENT_APP_NAME = context.getPackageName();
        this.options =opts;
        this.initialize();
    }

    public MWZMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        CLIENT_APP_NAME = context.getPackageName();
        this.options = getOptions(context,attrs);
        this.initialize();
    }

    public MWZMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CLIENT_APP_NAME = context.getPackageName();
        this.options = getOptions(context,attrs);
        this.initialize();

    }

    public void setListener(MWZMapViewListener listener) {
        this.listener = listener;
    }

    private MWZMapOptions getOptions(Context context, AttributeSet attrs) {

        MWZMapOptions opts = new MWZMapOptions();
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MWZMapView,
                0, 0);
        try {
            String apiKey = a.getString(R.styleable.MWZMapView_apikey);
            if (apiKey != null){
                opts.setApiKey(apiKey);
            }
            String mainColor = a.getString(R.styleable.MWZMapView_mainColor);
            if (mainColor != null){
                opts.setMainColor(mainColor);
            }
            opts.setIsLocationEnabled(a.getBoolean(R.styleable.MWZMapView_isLocationEnabled, Boolean.TRUE));
            opts.setIsBeaconsEnabled(a.getBoolean(R.styleable.MWZMapView_isBeaconsEnabled, Boolean.TRUE));
            opts.setShowUserPositionControl(a.getBoolean(R.styleable.MWZMapView_showUserPositionControl, Boolean.TRUE));
            opts.setDisplayFloorControl(a.getBoolean(R.styleable.MWZMapView_displayFloorControl, Boolean.TRUE));
            opts.setLanguage(a.getString(R.styleable.MWZMapView_language));

            String iconUrl = a.getString(R.styleable.MWZMapView_iconUrl);
            int xAnchor = a.getInteger(R.styleable.MWZMapView_x_iconAnchor, Integer.MAX_VALUE);
            int yAnchor = a.getInteger(R.styleable.MWZMapView_y_iconAnchor, Integer.MAX_VALUE);
            int xSize = a.getInteger(R.styleable.MWZMapView_x_iconSize, Integer.MAX_VALUE);
            int ySize = a.getInteger(R.styleable.MWZMapView_y_iconSize, Integer.MAX_VALUE);
            if (iconUrl != null) {
                if (xAnchor != Integer.MAX_VALUE && yAnchor != Integer.MAX_VALUE && xSize != Integer.MAX_VALUE && ySize != Integer.MAX_VALUE) {
                    Integer[] anchor = new Integer[2];
                    anchor[0] = xAnchor;
                    anchor[1] = yAnchor;
                    Integer[] size = new Integer[2];
                    size[0] = xSize;
                    size[1] = ySize;
                    opts.setDisplayMarkerOptions(new MWZCustomMarkerOptions(iconUrl, anchor, size));
                }
            }


            float centerLat = a.getFloat(R.styleable.MWZMapView_center_latitude, Float.MAX_VALUE);
            float centerLon = a.getFloat(R.styleable.MWZMapView_center_longitude, Float.MAX_VALUE);
            if (centerLat != Float.MAX_VALUE && centerLon != Float.MAX_VALUE) {
                opts.setCenter(new MWZCoordinate(new Double(centerLat), new Double(centerLon)));
            }
            int floor = a.getInteger(R.styleable.MWZMapView_floor, Integer.MAX_VALUE);
            if (floor != Integer.MAX_VALUE) {
                opts.setFloor(floor);
            }
            int zoom = a.getInteger(R.styleable.MWZMapView_zoom, Integer.MAX_VALUE);
            if (zoom != Integer.MAX_VALUE) {
                opts.setZoom(zoom);
            }
            opts.setZoomControl(false);
            String accessKey = a.getString(R.styleable.MWZMapView_accesskey);
            if (accessKey != null) {
                opts.setAccessKey(accessKey);
            }
            int minZoom = a.getInteger(R.styleable.MWZMapView_minZoom, 0);
            opts.setMinZoom(minZoom);

            float latMin = a.getFloat(R.styleable.MWZMapView_maxBounds_latitudeMin, Float.MAX_VALUE);
            float latMax = a.getFloat(R.styleable.MWZMapView_maxBounds_latitudeMax, Float.MAX_VALUE);
            float lngMin = a.getFloat(R.styleable.MWZMapView_maxBounds_longitudeMin, Float.MAX_VALUE);
            float lngMax = a.getFloat(R.styleable.MWZMapView_maxBounds_longitudeMax, Float.MAX_VALUE);

            if (latMin != Float.MAX_VALUE && latMax != Float.MAX_VALUE && lngMin != Float.MAX_VALUE && lngMax != Float.MAX_VALUE) {
                MWZBounds bounds = new MWZBounds(new MWZCoordinate((double)latMin, (double)lngMin), new MWZCoordinate((double)latMax,(double)lngMax));
                opts.setMaxBounds(bounds);
            }

            float latMinBounds = a.getFloat(R.styleable.MWZMapView_bounds_latitudeMin, Float.MAX_VALUE);
            float latMaxBounds = a.getFloat(R.styleable.MWZMapView_bounds_latitudeMax, Float.MAX_VALUE);
            float lngMinBounds = a.getFloat(R.styleable.MWZMapView_bounds_longitudeMin, Float.MAX_VALUE);
            float lngMaxBounds = a.getFloat(R.styleable.MWZMapView_bounds_longitudeMax, Float.MAX_VALUE);

            if (latMinBounds != Float.MAX_VALUE && latMaxBounds != Float.MAX_VALUE && lngMinBounds != Float.MAX_VALUE && lngMaxBounds != Float.MAX_VALUE) {
                MWZBounds bounds = new MWZBounds(new MWZCoordinate((double)latMinBounds, (double)lngMinBounds), new MWZCoordinate((double)latMaxBounds,(double)lngMaxBounds));
                opts.setBounds(bounds);
            }

        } finally {
            a.recycle();
        }
        return opts;
    }

    private void intializeLocationTools(){
        Context ctx = this.getContext();
        this.locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        this.beaconManager = BeaconManager.getInstanceForApplication(ctx);
        this.beaconManager.getBeaconParsers().add(0, new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        this.monitoredRegions = new Region[0];
        this.mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        backgroundPowerSaver = new BackgroundPowerSaver(ctx);
    }

    private void initialize() {
        this.intializeLocationTools();

        MWZAccountManager am = MWZAccountManager.getInstance();
        if (am.getApiKey() != null) {
            this.options.setApiKey(am.getApiKey());
        }

        // Debugger
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(this, true);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.addJavascriptInterface(this, "android");
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        this.setWebChromeClient(new MWZMapView.GeoWebChromeClient());


        if (options.getZoom() != null) {
            this.zoom = options.getZoom();
        }

        if (options.getFloor() != null) {
            this.floor = options.getFloor();
        }

        if (options.getCenter() != null) {
            this.center = options.getCenter();
        }

        this.loadUrl("file:///android_asset/mwzmap.html");
        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                initHtmlMap(options);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    if (listener != null) {
                        listener.onReceivedError(description);
                    }
                    if (!isLoaded) {
                        loadUrl("about:blank");
                    }
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (listener != null) {
                        listener.onReceivedError("" + error.getDescription());
                    }
                    if (!isLoaded) {
                        loadUrl("about:blank");
                    }
                }
            }
        });
    }

    private void initHtmlMap(MWZMapOptions options) {
        this.executeJS("Mapwize.config.SERVER = '" + SERVER_URL + "';");
        this.executeJS("Mapwize.config.SDK_NAME = '" + ANDROID_SDK_NAME + "';");
        this.executeJS("Mapwize.config.SDK_VERSION = '" + ANDROID_SDK_VERSION + "';");
        this.executeJS("Mapwize.config.CLIENT_APP_NAME = '" + CLIENT_APP_NAME + "';");
        if (options.isBeaconsEnabled()) {
            this.initString += "Mapwize.Location.on('monitoredUuidsChange', function(e){android.onMonitoredUuidsChange((function(){return JSON.stringify(e.uuids)})())});";
        }
        this.executeJS("var map = Mapwize.map('map'," + options.toJSONString() + ", function () {" + this.initString + "android.onMapLoad();})");
        this.isLoaded = true;
    }

    private void executeJS(final String js) {
        final MWZMapView self = this;
        this.post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    self.evaluateJavascript(js, null);
                } else {
                    self.loadUrl("javascript:" + js);
                }
            }
        });
    }

    /**
     * Javascript interface for event listener
     */

    @JavascriptInterface
    public void onMapLoad() {
        if (this.listener != null) {
            this.listener.onMapLoad();
            this.listener.onMapLoaded();
        }
        if (this.options.isLocationEnabled()) {
            if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (this.listener != null) {
                    this.listener.onMissingPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                return;
            }
            this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (this.options.isBeaconsEnabled()) {
            this.beaconManager.bind(this);
        }


    }

    @JavascriptInterface
    public void onZoomEnd(String value) {
        this.zoom = Integer.parseInt(value);
        if (this.listener != null) {
            this.listener.onZoomEnd(this.zoom);
        }
    }

    @JavascriptInterface
    public void onClick(String value) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            MWZCoordinate coordinate = mapper.readValue(value, MWZCoordinate.class);
            if (this.listener != null) {
                this.listener.onClick(coordinate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onContextMenu(String value) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            MWZCoordinate coordinate = mapper.readValue(value, MWZCoordinate.class);
            if (this.listener != null) {
                this.listener.onContextMenu(coordinate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onFloorChange(String value) {
        if (value != null){
            this.floor = Integer.parseInt(value);
        }
        else {
            this.floor = null;
        }
        if (this.listener != null) {
            this.listener.onFloorChange(this.floor);
        }
    }

    @JavascriptInterface
    public void onFloorsChange(String value) {
        try {
            this.floors = new ObjectMapper().readValue(value, Integer[].class);
            if (this.listener != null) {
                this.listener.onFloorsChange(this.floors);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onPlaceClick(String value) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            MWZPlace place = mapper.readValue(value, MWZPlace.class);
            if (this.listener != null) {
                this.listener.onPlaceClick(place);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onVenueClick(String value) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            MWZVenue venue = mapper.readValue(value, MWZVenue.class);
            if (this.listener != null) {
                this.listener.onVenueClick(venue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onMarkerClick(String value) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            MWZCoordinate coordinate = mapper.readValue(value, MWZCoordinate.class);
            if (this.listener != null) {
                this.listener.onMarkerClick(coordinate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onMoveEnd(String value) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            MWZCoordinate coordinate = mapper.readValue(value, MWZCoordinate.class);
            this.center = coordinate;
            if (this.listener != null) {
                this.listener.onMoveEnd(coordinate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onUserPositionChange(String value) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            MWZUserPosition coordinate = mapper.readValue(value, MWZUserPosition.class);
            this.userPosition = coordinate;
            if (this.listener != null) {
                this.listener.onUserPositionChange(coordinate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onFollowUserModeChange(String value) {
        this.followUserMode = Boolean.parseBoolean(value);
        if (this.listener != null) {
            this.listener.onFollowUserModeChange(this.followUserMode);
        }
    }

    @JavascriptInterface
    public void onDirectionsStart(String value) {
        if (this.listener != null) {
            this.listener.onDirectionsStart(value);
        }
    }

    @JavascriptInterface
    public void onDirectionsStop(String value) {
        if (this.listener != null) {
            this.listener.onDirectionsStop(value);
        }
    }

    @JavascriptInterface
    public void onMonitoredUuidsChange(String value) {
        String[] uuids = new String[0];
        try {
            uuids = new ObjectMapper().readValue(value, String[].class);
            this.updateMonitoredUuids(uuids);
            if (this.listener != null) {
                this.listener.onMonitoredUuidsChange(uuids);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onLoadUrlResponse(String value) {
        try {
            JSONObject jObject = new JSONObject(value);
            String hash = jObject.getString("hash");
            LoadURLCallbackInterface callback = (LoadURLCallbackInterface) callbackMemory.get(hash);
            if (!"".equals(jObject.getString("error"))) {
                Error error = new Error("MWZErrorDomain");
                callback.onResponse(error);
            } else {
                callback.onResponse(null);
            }
            callbackMemory.remove(hash);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void onUniverseChange(String value) {
        try {
            JSONObject jObject = new JSONObject(value);
            String venueId = jObject.getString("venueId");
            String universeId = jObject.getString("universeId");
            this.universesByVenues.put(venueId, universeId);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Getters
     */
    public Integer getFloor() {
        return floor;
    }

    public Integer[] getFloors() {
        return floors;
    }

    public Integer getZoom() {
        return zoom;
    }

    public boolean isFollowUserMode() {
        return followUserMode;
    }

    public MWZCoordinate getCenter() {
        return center;
    }

    public MWZUserPosition getUserPosition() {
        return userPosition;
    }

    /**
     *  Mapwize.js interface methods
     */
    public void fitBounds(@NonNull MWZBounds bounds) {
        Double latNW = bounds.getNorthEast().getLatitude();
        Double lonNW = bounds.getNorthEast().getLongitude();
        Double latSE = bounds.getSouthWest().getLatitude();
        Double lonSE = bounds.getSouthWest().getLongitude();
        this.executeJS("map.fitBounds(new L.LatLngBounds(new L.LatLng(" + latNW + ", " + lonNW + "), new L.LatLng(" + latSE + ", " + lonSE + ")));");
    }

    public void centerOnCoordinates(@NonNull MWZCoordinate coordinate) {
        centerOnCoordinates(coordinate, null);
    }

    public void centerOnCoordinates(@NonNull MWZCoordinate coordinate, @Nullable Integer zoom) {
        if (coordinate.getFloor() == null && zoom == null) {
            this.executeJS("map.centerOnCoordinates(" + coordinate.getLatitude() + "," + coordinate.getLongitude() + ")");
        } else if (coordinate.getFloor() == null) {
            this.executeJS("map.centerOnCoordinates(" + coordinate.getLatitude() + "," + coordinate.getLongitude() + ",null," + zoom + ")");
        } else if (zoom == null) {
            this.executeJS("map.centerOnCoordinates(" + coordinate.getLatitude() + "," + coordinate.getLongitude() + "," + coordinate.getFloor() + ",null)");
        } else {
            this.executeJS("map.centerOnCoordinates(" + coordinate.getLatitude() + "," + coordinate.getLongitude() + "," + coordinate.getFloor() + "," + zoom + ")");
        }
    }

    public void setFloor(@Nullable Integer floor) {
        this.executeJS("map.setFloor(" + floor + ")");
    }

    public void setZoom(@Nullable Integer zoom) {
        this.executeJS("map.setZoom(" + zoom + ")");
    }

    public void centerOnVenue(@NonNull MWZVenue venue) {
        this.executeJS("map.centerOnVenue('" + venue.getIdentifier() + "')");
    }

    public void centerOnVenue(@NonNull String id) {
        this.executeJS("map.centerOnVenue('" + id + "')");
    }

    public void centerOnPlace(@NonNull MWZPlace place) {
        this.executeJS("map.centerOnPlace('" + place.getIdentifier() + "')");
    }

    public void centerOnPlace(@NonNull String id) {
        this.executeJS("map.centerOnPlace('" + id + "')");
    }

    public void setFollowUserMode(boolean follow) {
        this.executeJS("map.setFollowUserMode(" + follow + ")");
    }

    public void centerOnUser(@Nullable Integer zoom) {
        this.executeJS("map.centerOnUser('" + zoom + "')");
    }

    public void newUserPositionMeasurement(@NonNull MWZMeasurement measurement) {
        this.executeJS("Mapwize.Location.newUserPositionMeasurement(" + measurement.toJSONString() + ")");
    }

    public void setUserHeading(@Nullable Double heading) {
        this.executeJS("Mapwize.Location.setUserHeading(" + heading + ")");
    }

    public void setUserPosition(@NonNull MWZUserPosition userPosition) {
        this.executeJS("Mapwize.Location.setUserPosition(" + userPosition.toJSONString() + ")");
    }

    public void removeUserPosition() {
        this.executeJS("Mapwize.Location.setUserPosition(null)");
    }

    public void unlockUserPosition() {
        this.executeJS("Mapwize.Location.unlockUserPosition()");
    }

    public void loadURL(@NonNull String url, @NonNull LoadURLCallbackInterface callback) {
        String hash = new RandomString(16).nextString();
        callbackMemory.put(hash, callback);
        this.executeJS("map.loadUrl('" + url + "',function(err){map.fire('apiResponse', {returnedType:'loadUrl', hash:'" + hash + "', error:err?'error':''});})");
    }

    public void addMarker(@NonNull MWZCoordinate coordinate) {
        this.executeJS("map.addMarker(" + coordinate.toJSONString() + ")");
    }

    @Deprecated
    public void addMarker(@NonNull Double latitude, @NonNull Double longitude, @Nullable Integer floor) {
        MWZCoordinate latLonFloor = new MWZCoordinate(latitude, longitude, floor);
        this.executeJS("map.addMarker(" + latLonFloor.toJSONString() + ")");
    }

    public void addMarker(@NonNull String placeId) {
        this.executeJS("map.addMarker({\"placeId\":'" + placeId + "'})");
    }

    public void removeMarkers() {
        this.executeJS("map.removeMarkers()");
    }

    public void startDirections(@NonNull MWZDirection direction) {
        this.executeJS("map.startDirections(" + direction.toJSONString() + ")");
    }

    public void stopDirections() {
        this.executeJS("map.stopDirections()");
    }

    public void setStyle(@NonNull String placeId, @NonNull MWZStyle style) {
        this.executeJS("map.setPlaceStyle('" + placeId + "'," + style.toJSONString() + ")");
    }

    public void setPreferredLanguage(@NonNull String language) {
        this.executeJS("map.setPreferredLanguage('" + language + "');");
    }

    public String getUniverseForVenue(MWZVenue venue) {
        return this.universesByVenues.get(venue.getIdentifier());
    }

    public void setUniverseForVenue(String universeId, MWZVenue venue) {
        this.executeJS("map.setUniverseForVenue('"+universeId+"','"+venue.getIdentifier()+"');");
    }

    public void setUniverseForVenue(MWZUniverse universe, MWZVenue venue) {
        this.setUniverseForVenue(universe.getIdentifier(), venue);
    }

    /* Promote place */
    public void setPromotedPlaces(List<MWZPlace> places) {
        if (places == null) {
            this.setPromotedPlacesWithIds(null);
        }
        else {
            List<String> placeIds = new ArrayList<>();
            for (MWZPlace place : places) {
                placeIds.add(place.getIdentifier());
            }
            setPromotedPlacesWithIds(placeIds);
        }
    }

    public void setPromotedPlacesWithIds(List<String> placeIds) {
        if (placeIds == null) {
            this.executeJS("map.setPromotePlaces(null);");
        }
        else {
            try {
                String jsonInString = new ObjectMapper().writeValueAsString(placeIds);
                this.executeJS("map.setPromotePlaces("+jsonInString+");");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPromotedPlace(MWZPlace place) {
        this.addPromotedPlaceWithId(place.getIdentifier());
    }

    public void addPromotedPlaceWithId(String placeId) {
        this.executeJS("map.addPromotePlace('"+placeId+"');");
    }

    public void addPromotedPlaces(List<MWZPlace> places) {
        if (places != null) {
            List<String> placeIds = new ArrayList<>();
            for (MWZPlace place : places) {
                placeIds.add(place.getIdentifier());
            }
            addPromotedPlacesWithIds(placeIds);
        }
    }

    public void addPromotedPlacesWithIds(List<String> placeIds) {
        if (placeIds != null) {
            try {
                String jsonInString = new ObjectMapper().writeValueAsString(placeIds);
                this.executeJS("map.addPromotePlaces("+jsonInString+");");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void removePromotedPlace(MWZPlace place) {
        this.removePromotedPlaceWithId(place.getIdentifier());
    }

    public void removePromotedPlaceWithId(String placeId) {
        this.executeJS("map.removePromotePlace('"+placeId+"');");
    }

    /* Ignore place */
    public void addIgnoredPlace(MWZPlace place) {
        this.addIgnoredPlaceWithId(place.getIdentifier());
    }

    public void addIgnoredPlaceWithId(String identifier) {
        this.executeJS("map.addIgnorePlace('"+identifier+"');");
    }

    public void removeIgnoredPlace(MWZPlace place) {
        this.removeIgnoredPlaceWithId(place.getIdentifier());
    }

    public void removeIgnoredPlaceWithId(String identifier) {
        this.executeJS("map.removeIgnorePlace('"+identifier+"');");
    }

    public void setIgnoredPlaces(List<MWZPlace> places) {
        if (places == null) {
            this.setIgnoredPlacesWithIds(null);
        }
        else {
            List<String> placeIds = new ArrayList<>();
            for (MWZPlace place : places) {
                placeIds.add(place.getIdentifier());
            }
            this.setIgnoredPlacesWithIds(placeIds);
        }
    }

    public void setIgnoredPlacesWithIds(List<String> placeIds) {
        if (placeIds == null) {
            this.executeJS("map.setIgnorePlaces(null);");
        }
        else {
            try {
                String jsonInString = new ObjectMapper().writeValueAsString(placeIds);
                this.executeJS("map.setIgnorePlaces("+jsonInString+");");
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void setExternalPlaces(List<MWZPlace> places) {
        try {
            String jsonInString = new ObjectMapper().writeValueAsString(places);
            this.executeJS("map.setExternalPlaces("+jsonInString+");");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void setBottomMargin(@NonNull Integer margin) {
        this.executeJS("map.setBottomMargin(" + margin + ");");
    }

    public void setTopMargin(@NonNull Integer margin) {
        this.executeJS("map.setTopMargin(" + margin + ");");
    }


    public void refresh() {
        CookieManager cm = CookieManager.getInstance();
        this.executeJS("map.refresh()");
    }

    public void addRangedIBeacons(@NonNull String uuid, @NonNull Beacon[] beacons) {
        JSONArray beaconsArray = null;
        try {
            beaconsArray = new JSONArray();
            for (int i = 0; i < beacons.length; i++) {
                Beacon b = beacons[i];
                JSONObject o = new JSONObject();
                o.put("uuid", b.getId1());
                o.put("major", b.getId2());
                o.put("minor", b.getId3());
                o.put("rssi", b.getRssi());
                beaconsArray.put(o);
            }
            String beaconsString = beaconsArray.toString();
            this.executeJS("Mapwize.Location.addRangedIBeacons('" + uuid + "'," + beaconsString + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateMonitoredUuids(@NonNull String[] uuids) {
        try {
            for (int i = 0; i < this.monitoredRegions.length; i++) {
                beaconManager.stopMonitoringBeaconsInRegion(this.monitoredRegions[i]);
            }
            Region[] rg = new Region[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                Region r = new Region(uuids[i], Identifier.parse(uuids[i]), null, null);
                beaconManager.startRangingBeaconsInRegion(r);
                rg[i] = r;
            }
            this.monitoredRegions = rg;
        } catch (RemoteException e) {
        }
    }

    private void updateMonitoredUuids(@NonNull Region[] regions) {
        Region[] rg = new Region[regions.length];
        try {
            for (int i = 0; i < this.monitoredRegions.length; i++) {
                beaconManager.stopMonitoringBeaconsInRegion(this.monitoredRegions[i]);
            }
            for (int i = 0; i < regions.length; i++) {
                beaconManager.startRangingBeaconsInRegion(regions[i]);
                rg[i] = regions[i];
            }
            this.monitoredRegions = rg;
        } catch (RemoteException e) {
        }
    }

    public void startLocation(boolean useBeacon) {
        this.unlockUserPosition();
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (this.listener != null) {
                this.listener.onMissingPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            return;
        }
        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        this.updateMonitoredUuids(new String[0]);
        this.options.setIsBeaconsEnabled(useBeacon);
        if (useBeacon) {
            this.updateMonitoredUuids(this.monitoredRegions);
        }
    }

    public void stopLocation() {
        this.locationManager.removeUpdates(this);
        this.mSensorManager.unregisterListener(this);
        this.setUserHeading(null);
        this.removeUserPosition();
        this.options.setIsBeaconsEnabled(false);
        this.monitoredRegions = new Region[0];
    }

    /**
     * Location
     * @param location
     */

    @Override
    public void onLocationChanged(@Nullable Location location) {
        MWZMeasurement m = new MWZMeasurement(location.getLatitude(), location.getLongitude(), null, (int)(Math.floor(location.getAccuracy())),null,null, "gps");
        this.newUserPositionMeasurement(m);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Beacon[] beaconsArray = new Beacon[beacons.size()];
                int i=0;
                for (Beacon b : beacons) {
                    beaconsArray[i++] = b;
                }
                addRangedIBeacons(region.getId1().toString(), beaconsArray);
            }
        });
    }

    @Override
    public Context getApplicationContext() {
        return this.getContext().getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        this.getContext().unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return this.getContext().bindService(intent, serviceConnection, i);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentDegree = Math.round(event.values[0]);
        this.setUserHeading(Double.valueOf(currentDegree));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Add geolocation prompt to WebChromeClient
     */
    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);
        }
    }

    public void onDestroy() {
        if (this.options.isBeaconsEnabled()) {
            beaconManager.unbind(this);
        }
    }

}
