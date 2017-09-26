package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MWZBounds {

    private MWZCoordinate northEast;
    private MWZCoordinate southWest;

    public MWZBounds() {
        super();
    }

    public MWZBounds (MWZCoordinate northEast, MWZCoordinate southWest) {
        super();
        this.northEast = northEast;
        this.southWest = southWest;
    }

    @JsonCreator
    public MWZBounds (Double[] array) {
        this.southWest = new MWZCoordinate(array[0],array[1]);
        this.northEast = new MWZCoordinate(array[2],array[3]);
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

    @JsonValue
    public Double[][] toArray() {
        Double[][] result = new Double[2][2];
        result[0][0] = this.northEast.getLatitude();
        result[0][1] = this.northEast.getLongitude();
        result[1][0] = this.southWest.getLatitude();
        result[1][1] = this.southWest.getLongitude();

        return result;
    }

    public MWZCoordinate getNorthEast() {
        return northEast;
    }

    public void setNorthEast(MWZCoordinate northEast) {
        this.northEast = northEast;
    }

    public MWZCoordinate getSouthWest() {
        return southWest;
    }

    public void setSouthWest(MWZCoordinate southWest) {
        this.southWest = southWest;
    }
}
