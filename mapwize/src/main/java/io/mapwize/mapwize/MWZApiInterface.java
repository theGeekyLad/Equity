package io.mapwize.mapwize;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

interface MWZApiInterface {

    /*
      Access request related method
    */
    @GET("access/{key}")
    Call<Map<String,Object>[]> getAccess(@Path("key") String accessKey, @Query("api_key") String apiKey);

    /*
      Universes request related method
     */
    @GET("universes")
    Call<List<MWZUniverse>> getUniverses(@QueryMap Map<String,String> options, @Query("api_key") String apiKey);

    /*
      Venues request related methods
    */
    @GET("venues")
    Call<List<MWZVenue>> getVenues(@QueryMap Map<String,String> options, @Query("api_key") String apiKey);

    @GET("venues/{id}")
    Call<MWZVenue> getVenueWithId(@Path("id") String id, @Query("api_key") String apiKey);

    /*
      Places request related methods
    */
    @GET("places")
    Call<List<MWZPlace>> getPlaces(@QueryMap Map<String,String> options, @Query("api_key") String apiKey);

    @GET("places/{id}")
    Call<MWZPlace> getPlaceWithId(@Path("id") String id, @Query("api_key") String apiKey);

    /*
      PlaceLists request related methods
    */
    @GET("placeLists")
    Call<List<MWZPlaceList>> getPlaceLists(@QueryMap Map<String,String> options, @Query("api_key") String apiKey);

    @GET("placeLists/{id}")
    Call<MWZPlaceList> getPlaceListWithId(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("placeLists/{id}/places")
    Call<List<MWZPlace>> getPlacesForPlaceList(@Path("id") String id, @Query("api_key") String apiKey);

    /*
      Directions request related methods
    */
    @POST("directions")
    Call<MWZDirection> getDirection(@Body MWZDirectionRequestObject directionRequestObject, @Query("api_key") String apiKey);

    /*
      Search request
    */
    @POST("search")
    Call<MWZSearchResponse> performSearch(@Query("api_key") String apiKey, @Body MWZSearchParams params);


}
