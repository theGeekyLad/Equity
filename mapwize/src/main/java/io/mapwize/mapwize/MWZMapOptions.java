package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MWZMapOptions {

    private String apiKey;
    private MWZBounds maxBounds;
    private MWZBounds bounds;
    private MWZCoordinate center;
    private Integer zoom;
    private Integer floor;
    private boolean zoomControl;
    private boolean showUserPositionControl = true;
    private boolean displayFloorControl = true;
    private String accessKey;
    private String language;
    private Integer minZoom;
    private String outdoorMapProvider;
    private String mainColor;
    private boolean isLocationEnabled;
    private boolean isBeaconsEnabled;
    private boolean useBrowserLocation = false;
    private MWZCustomMarkerOptions displayMarkerOptions;

    public boolean isUseBrowserLocation() {
        return useBrowserLocation;
    }

    public MWZMapOptions() {
        this.isBeaconsEnabled = true;
        this.isLocationEnabled = true;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public MWZCustomMarkerOptions getDisplayMarkerOptions() {
        return displayMarkerOptions;
    }

    public void setDisplayMarkerOptions(MWZCustomMarkerOptions displayMarkerOptions) {
        this.displayMarkerOptions = displayMarkerOptions;
    }

    public boolean isDisplayFloorControl() {
        return displayFloorControl;
    }

    public void setDisplayFloorControl(boolean displayFloorControl) {
        this.displayFloorControl = displayFloorControl;
    }

    public boolean isLocationEnabled() {
        return isLocationEnabled;
    }

    public void setIsLocationEnabled(boolean isLocationEnabled) {
        this.isLocationEnabled = isLocationEnabled;
    }

    public MWZBounds getBounds() {
        return bounds;
    }

    public void setBounds(MWZBounds bounds) {
        this.bounds = bounds;
    }

    public boolean getShowUserPositionControl() {
        return showUserPositionControl;
    }

    public void setShowUserPositionControl(boolean showUserPositionControl) {
        this.showUserPositionControl = showUserPositionControl;
    }

    public boolean isBeaconsEnabled() {
        return isBeaconsEnabled;
    }

    public void setIsBeaconsEnabled(boolean isBeaconsEnabled) {
        this.isBeaconsEnabled = isBeaconsEnabled;
    }

    public boolean isZoomControl() {
        return zoomControl;
    }

    public void setZoomControl(boolean zoomControl) {
        this.zoomControl = zoomControl;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public MWZBounds getMaxBounds() {
        return maxBounds;
    }

    public void setMaxBounds(MWZBounds maxBounds) {
        this.maxBounds = maxBounds;
    }

    public MWZCoordinate getCenter() {
        return center;
    }

    public void setCenter(MWZCoordinate center) {
        this.center = center;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public String getOutdoorMapProvider() {
        return outdoorMapProvider;
    }

    public void setOutdoorMapProvider(String outdoorMapProvider) {
        this.outdoorMapProvider = outdoorMapProvider;
    }

    public String toString() {
        return "MWZMapOptions : ApiKey="+apiKey+" MaxBounds="+maxBounds+" Center="+center+" Zoom="+zoom+" Floor="+floor+" LocationEnabled="+this.isLocationEnabled+" BeaconEnabled="+this.isBeaconsEnabled;
    }

    public String toJSONString() {
        String jsonInString = null;
        try {
            jsonInString = new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonInString;
    }

}
