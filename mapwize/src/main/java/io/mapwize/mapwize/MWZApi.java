package io.mapwize.mapwize;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MWZApi {

    private static MWZApiInterface sApiInterface;
    private static String sApiKey;

    static {
        try {
            MWZAccountManager am = MWZAccountManager.getInstance();
            sApiInterface = MWZRetrofitBuilder.getComplexClient(am.getContext());
            sApiKey = am.getApiKey();
        }
        catch (Exception e) {
            throw new IllegalStateException("MWZApi is called before MWZAccountManager is configured");
        }
    }

    public static void getAccess(@NonNull String access, @NonNull final MWZCallback<Map<String,Object>[]> callback) {
        Call<Map<String, Object>[]> call = sApiInterface.getAccess(access, sApiKey);
        call.enqueue(new Callback<Map<String,Object>[]>() {
            @Override
            public void onResponse(Call<Map<String, Object>[]> call, Response<Map<String, Object>[]> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>[]> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getUniversesForOrganizationId(@NonNull String organizationId, @NonNull final MWZCallback<List<MWZUniverse>> callback) {
        if (organizationId == null || "".equals(organizationId)) {
            callback.onFailure(new Throwable("An organizationId must be provided"));
        }
        else {
            Map<String,String> opts = new HashMap<>();
            opts.put("organizationId", organizationId);
            Call<List<MWZUniverse>> call = sApiInterface.getUniverses(opts, sApiKey);
            call.enqueue(new Callback<List<MWZUniverse>>() {
                @Override
                public void onResponse(Call<List<MWZUniverse>> call, Response<List<MWZUniverse>> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    }
                    else {
                        try {
                            callback.onFailure(new Throwable(response.errorBody().string()));
                        } catch (IOException e) {
                            callback.onFailure(new Throwable("Mapwize request error"));
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<MWZUniverse>> call, Throwable t) {
                    callback.onFailure(t);
                }
            });
        }
    }

    public static void getVenues(@NonNull final MWZCallback<List<MWZVenue>> callback) {
        getVenues(new HashMap<String, String>(), callback);
    }

    public static void getVenuesWithOrganizationId(@NonNull String organizationId, @NonNull final MWZCallback<List<MWZVenue>> callback) {
        Map<String,String> opts = new HashMap<>();
        opts.put("organizationId", organizationId);
        getVenues(opts, callback);
    }

    public static void getVenues(@NonNull Map<String,String> options, @NonNull final MWZCallback<List<MWZVenue>> callback) {
        Call<List<MWZVenue>> call = sApiInterface.getVenues(options, sApiKey);
        call.enqueue(new Callback<List<MWZVenue>>() {

            @Override
            public void onResponse(Call<List<MWZVenue>> call, Response<List<MWZVenue>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MWZVenue>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getVenueWithId(@NonNull String id, @NonNull final MWZCallback<MWZVenue> callback) {
        Call<MWZVenue> call = sApiInterface.getVenueWithId(id, sApiKey);
        call.enqueue(new Callback<MWZVenue>() {

            @Override
            public void onResponse(Call<MWZVenue> call, Response<MWZVenue> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<MWZVenue> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getVenueWithName(@NonNull String name, @NonNull final MWZCallback<MWZVenue> callback) {
        Map<String,String> options = new HashMap<>();
        options.put("name", name);
        getVenues(options, new MWZCallback<List<MWZVenue>>() {
            @Override
            public void onSuccess(List<MWZVenue> object) {
                if (object.size() > 0) {
                    callback.onSuccess(object.get(0));
                }
                else {
                    callback.onFailure(new Throwable("Object not found"));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getVenueWithAlias(@NonNull String alias, @NonNull final MWZCallback<MWZVenue> callback) {
        Map<String,String> options = new HashMap<>();
        options.put("alias", alias);
        getVenues(options, new MWZCallback<List<MWZVenue>>() {
            @Override
            public void onSuccess(List<MWZVenue> object) {
                if (object.size() > 0) {
                    callback.onSuccess(object.get(0));
                }
                else {
                    callback.onFailure(new Throwable("Object not found"));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getPlaces(@NonNull final MWZCallback<List<MWZPlace>> callback) {
        getPlaces(new HashMap<String, String>(), callback);
    }

    public static void getPlaces(@NonNull Map<String,String> options, @NonNull final MWZCallback<List<MWZPlace>> callback) {
        Call<List<MWZPlace>> call = sApiInterface.getPlaces(options, sApiKey);
        call.enqueue(new Callback<List<MWZPlace>>() {

            @Override
            public void onResponse(Call<List<MWZPlace>> call, Response<List<MWZPlace>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MWZPlace>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getPlaceWithId(@NonNull String id, @NonNull final MWZCallback<MWZPlace> callback) {
        Call<MWZPlace> call = sApiInterface.getPlaceWithId(id, sApiKey);
        call.enqueue(new Callback<MWZPlace>() {

            @Override
            public void onResponse(Call<MWZPlace> call, Response<MWZPlace> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<MWZPlace> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getPlaceWithName(@NonNull String name, @NonNull MWZVenue venue, @NonNull final MWZCallback<MWZPlace> callback) {
        Map<String,String> options = new HashMap<>();
        options.put("name", name);
        options.put("venueId", venue.getIdentifier());
        getPlaces(options, new MWZCallback<List<MWZPlace>>() {
            @Override
            public void onSuccess(List<MWZPlace> object) {
                if (object.size() > 0) {
                    callback.onSuccess(object.get(0));
                }
                else {
                    callback.onFailure(new Throwable("Object not found"));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getPlaceWithAlias(@NonNull String alias, @NonNull MWZVenue venue, @NonNull final MWZCallback<MWZPlace> callback) {
        Map<String,String> options = new HashMap<>();
        options.put("alias", alias);
        options.put("venueId", venue.getIdentifier());
        getPlaces(options, new MWZCallback<List<MWZPlace>>() {
            @Override
            public void onSuccess(List<MWZPlace> object) {
                if (object.size() > 0) {
                    callback.onSuccess(object.get(0));
                }
                else {
                    callback.onFailure(new Throwable("Object not found"));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getPlacesForVenue(@NonNull MWZVenue venue, @NonNull final MWZCallback<List<MWZPlace>> callback) {
        Map<String,String> options = new HashMap<>();
        options.put("venueId", venue.getIdentifier());
        getPlaces(options, callback);
    }

    public void getPlaceLists(@NonNull final MWZCallback<List<MWZPlaceList>> callback) {
        getPlaceLists(new HashMap<String, String>(), callback);
    }

    public static void getPlaceLists(@NonNull Map<String,String> options, @NonNull final MWZCallback<List<MWZPlaceList>> callback) {
        Call<List<MWZPlaceList>> call = sApiInterface.getPlaceLists(options, sApiKey);
        call.enqueue(new Callback<List<MWZPlaceList>>() {

            @Override
            public void onResponse(Call<List<MWZPlaceList>> call, Response<List<MWZPlaceList>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MWZPlaceList>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getPlaceListWithId(@NonNull String id, @NonNull final MWZCallback<MWZPlaceList> callback) {
        Call<MWZPlaceList> call = sApiInterface.getPlaceListWithId(id, sApiKey);
        call.enqueue(new Callback<MWZPlaceList>() {

            @Override
            public void onResponse(Call<MWZPlaceList> call, Response<MWZPlaceList> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<MWZPlaceList> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getPlaceListWithName(@NonNull String name, @NonNull MWZVenue venue, @NonNull final MWZCallback<MWZPlaceList> callback) {
        Map<String,String> options = new HashMap<>();
        options.put("venueId", venue.getIdentifier());
        options.put("name", name);
        getPlaceLists(options, new MWZCallback<List<MWZPlaceList>>() {
            @Override
            public void onSuccess(List<MWZPlaceList> object) {
                if (object.size() > 0) {
                    callback.onSuccess(object.get(0));
                }
                else {
                    callback.onFailure(new Throwable("Object not found"));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getPlaceListWithAlias(@NonNull String alias, @NonNull MWZVenue venue, @NonNull final MWZCallback<MWZPlaceList> callback) {
        Map<String,String> options = new HashMap<>();
        options.put("venueId", venue.getIdentifier());
        options.put("alias", alias);
        getPlaceLists(options, new MWZCallback<List<MWZPlaceList>>() {
            @Override
            public void onSuccess(List<MWZPlaceList> object) {
                if (object.size() > 0) {
                    callback.onSuccess(object.get(0));
                }
                else {
                    callback.onFailure(new Throwable("Object not found"));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getPlaceListsForVenue(@NonNull MWZVenue venue, @NonNull final MWZCallback<List<MWZPlaceList>> callback) {
        Map<String, String> options = new HashMap<>();
        options.put("venueId", venue.getIdentifier());
        getPlaceLists(options, callback);
    }

    public static void getPlacesForPlaceList(@NonNull MWZPlaceList placeList, @NonNull final MWZCallback<List<MWZPlace>> callback) {
        Call<List<MWZPlace>> call = sApiInterface.getPlacesForPlaceList(placeList.getIdentifier(), sApiKey);
        call.enqueue(new Callback<List<MWZPlace>>() {
            @Override
            public void onResponse(Call<List<MWZPlace>> call, Response<List<MWZPlace>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MWZPlace>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public static void getDirection(@NonNull MWZDirectionPoint from, @Nullable MWZDirectionPoint to, @Nullable  List<MWZDirectionPoint> waypoints, @Nullable MWZDirectionOptions options, @NonNull final MWZCallback<MWZDirection> callback) {
        MWZDirectionRequestObject requestObject = new MWZDirectionRequestObject();
        if (to != null) {
            List<MWZDirectionPointWrapper> toList = new ArrayList<>();
            toList.add(to.toDirectionWrapper());
        }
        requestObject.from = from.toDirectionWrapper();
        if (to != null) {
            List<MWZDirectionPointWrapper> toList = new ArrayList<>();
            toList.add(to.toDirectionWrapper());
            requestObject.to = toList;
        }
        if (waypoints != null) {
            requestObject.waypoints = new ArrayList<>();
            for (MWZDirectionPoint wp : waypoints) {
                requestObject.waypoints.add(wp.toDirectionWrapper());
            }
        }
        requestObject.options = options;

        Call<MWZDirection> call = sApiInterface.getDirection(requestObject, sApiKey);
        call.enqueue(new Callback<MWZDirection>(){

            @Override
            public void onResponse(Call<MWZDirection> call, Response<MWZDirection> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<MWZDirection> call, Throwable t) {
                callback.onFailure(new Throwable("Bad request " + t));
            }
        });
    }

    public static void getDirection(@NonNull MWZDirectionPoint from, @Nullable List<MWZDirectionPoint> to, @Nullable  List<MWZDirectionPoint> waypoints, @Nullable MWZDirectionOptions options, @NonNull final MWZCallback<MWZDirection> callback) {
        MWZDirectionRequestObject requestObject = new MWZDirectionRequestObject();
        requestObject.from = from.toDirectionWrapper();
        if (to != null) {
            List<MWZDirectionPointWrapper> toList = new ArrayList<>();
            for (MWZDirectionPoint wp : to) {
                toList.add(wp.toDirectionWrapper());
            }
            requestObject.to = toList;
        }
        if (waypoints != null) {
            requestObject.waypoints = new ArrayList<>();
            for (MWZDirectionPoint wp : waypoints) {
                requestObject.waypoints.add(wp.toDirectionWrapper());
            }
        }
        requestObject.options = options;

        Call<MWZDirection> call = sApiInterface.getDirection(requestObject, sApiKey);
        call.enqueue(new Callback<MWZDirection>(){

            @Override
            public void onResponse(Call<MWZDirection> call, Response<MWZDirection> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<MWZDirection> call, Throwable t) {
                callback.onFailure(new Throwable("Bad request " + t));
            }
        });
    }

    public static void search(@NonNull MWZSearchParams searchParams, @NonNull final MWZCallback<List<MWZSearchable>> callback) {
        Call<MWZSearchResponse> call = sApiInterface.performSearch(sApiKey, searchParams);
        call.enqueue(new Callback<MWZSearchResponse>() {

            @Override
            public void onResponse(Call<MWZSearchResponse> call, Response<MWZSearchResponse> response) {
                if (response.isSuccessful()) {

                    List<MWZSearchable> responseList = new ArrayList<MWZSearchable>();
                    for (Map<String,Object> map : response.body().getHits()) {
                        String type = (String)map.get("objectClass");

                        String jsonInString = null;
                        ObjectMapper mapper = new ObjectMapper();
                        MWZSearchable mwzSearchable = null;
                        try {
                            jsonInString = mapper.writeValueAsString(map);

                            if ("place".equals(type)){
                                mwzSearchable = mapper.readValue(jsonInString, MWZPlace.class);
                            }
                            else if ("venue".equals(type)){
                                mwzSearchable = mapper.readValue(jsonInString, MWZVenue.class);
                            }
                            else if ("placeList".equals(type)){
                                mwzSearchable = mapper.readValue(jsonInString, MWZPlaceList.class);
                            }
                            if (mwzSearchable != null) {
                                responseList.add(mwzSearchable);
                            }

                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    callback.onSuccess(responseList);
                }
                else {
                    try {
                        callback.onFailure(new Throwable(response.errorBody().string()));
                    } catch (IOException e) {
                        callback.onFailure(new Throwable("Mapwize request error"));
                    }
                }
            }

            @Override
            public void onFailure(Call<MWZSearchResponse> call, Throwable t) {
                callback.onFailure(new Throwable("Bad request " + t));
            }
        });
    }

}


