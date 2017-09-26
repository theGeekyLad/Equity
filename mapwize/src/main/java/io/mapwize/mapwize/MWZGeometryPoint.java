package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZGeometryPoint extends MWZGeometry {

    private List<Double> coordinates = new ArrayList<Double>();

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public MWZBounds getBounds() {
        MWZCoordinate coord = new MWZCoordinate(getCoordinates().get(1), getCoordinates().get(0));
        return new MWZBounds(coord, coord);
    }
}
