package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZUserPosition extends MWZCoordinate implements  MWZDirectionPoint{

    private Integer accuracy;

    public MWZUserPosition() {
        super();
    }

    public MWZUserPosition(Double latitude, Double longitude, Integer floor, Integer accuracy) {
        super(latitude, longitude, floor);
        this.accuracy = accuracy;
    }

    public MWZUserPosition(MWZCoordinate coordinate, Integer accuracy) {
        this(coordinate.getLatitude(), coordinate.getLongitude(), coordinate.getFloor(), accuracy);
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public String toString() {
        return "Latitude="+getLatitude()+" Longitude="+getLongitude()+" Floor="+getFloor()+" Accuracy="+accuracy;
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
        pw.setLatitude(getLatitude());
        pw.setLongitude(getLongitude());
        pw.setFloor(getFloor());
        return pw;
    }
}
