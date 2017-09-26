package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZGeometryPolygon extends MWZGeometry {

    private List<List<List<Double>>> coordinates = new ArrayList<List<List<Double>>>();

    public List<List<List<Double>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        this.coordinates = coordinates;
    }

    public MWZBounds getBounds() {
        double latMin = 400, latMax = -400, lonMin=400, lonMax=-400;

        for (List<List<Double>> l1 : coordinates) {
            for (List<Double> c : l1) {
                if (c.get(1) < latMin) {
                    latMin = c.get(1);
                }
                if (c.get(1) > latMax) {
                    latMax = c.get(1);
                }
                if (c.get(0) < lonMin) {
                    lonMin = c.get(0);
                }
                if (c.get(0) > lonMax) {
                    lonMax = c.get(0);
                }
            }
        }
        MWZCoordinate sw = new MWZCoordinate(latMin,latMax);
        MWZCoordinate ne = new MWZCoordinate(lonMin,lonMax);

        return new MWZBounds(sw, ne);
    }
}
