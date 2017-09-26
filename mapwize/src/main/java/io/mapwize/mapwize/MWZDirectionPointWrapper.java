package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class MWZDirectionPointWrapper {

    private Double latitude;
    private Double longitude;
    private Integer floor;
    private String venueId;
    private String placeId;
    private String placeListId;

    public MWZDirectionPointWrapper() {
        super();
    }

    @JsonGetter("lat")
    public Double getLatitude() {
        return latitude;
    }

    @JsonSetter("lat")
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonGetter("lon")
    public Double getLongitude() {
        return longitude;
    }

    @JsonSetter("lon")
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceListId() {
        return placeListId;
    }

    public void setPlaceListId(String placeListId) {
        this.placeListId = placeListId;
    }

    public String toString() {
        return "lat="+latitude+" lon="+longitude+" floor="+floor+" venueId="+venueId+" placeId="+placeId+" placelistId="+placeListId;
    }
}
