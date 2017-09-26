package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MWZCoordinate implements MWZDirectionPoint {

    private Double latitude;
    private Double longitude;
    private Integer floor;

    public MWZCoordinate() {
        super();
    }

    public MWZCoordinate(Double latitude, Double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MWZCoordinate(Double latitude, Double longitude, Integer floor) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.floor = floor;
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


    @Override
    public MWZDirectionPointWrapper toDirectionWrapper() {
        MWZDirectionPointWrapper pw = new MWZDirectionPointWrapper();
        pw.setLatitude(latitude);
        pw.setLongitude(longitude);
        pw.setFloor(floor);
        return pw;
    }

    public String toString() {
        return "Latitude="+latitude+" Longitude="+longitude+" Floor="+floor;
    }

    public Double getLatitude() {
        return latitude;
    }

    @JsonSetter("lat")
    public void setLat(Double latitude) {this.latitude = latitude;}

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonSetter("lon")
    public void setLon(Double longitude) {
        this.longitude = longitude;
    }

    @JsonSetter("lng")
    public void setLng(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }


}
