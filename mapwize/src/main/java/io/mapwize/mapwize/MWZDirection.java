package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MWZDirection {

    private MWZDirectionPointWrapper from;
    private MWZDirectionPointWrapper to;
    private Double distance;
    private Double traveltime;
    private MWZRoute[] route;
    private MWZBounds bounds;
    private MWZDirectionPointWrapper[] waypoints;
    private MWZDirection[] subdirections;

    public MWZDirection(){
        super();
    }

    public MWZDirectionPointWrapper getFrom() {
        return from;
    }

    public void setFrom(MWZDirectionPointWrapper from) {
        this.from = from;
    }

    public MWZDirectionPointWrapper getTo() {
        return to;
    }

    public void setTo(MWZDirectionPointWrapper to) {
        this.to = to;
    }

    public MWZDirectionPointWrapper[] getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(MWZDirectionPointWrapper[] waypoints) {
        this.waypoints = waypoints;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getTraveltime() {
        return traveltime;
    }

    public void setTraveltime(Double traveltime) {
        this.traveltime = traveltime;
    }

    public MWZRoute[] getRoute() {
        return route;
    }

    public void setRoute(MWZRoute[] route) {
        this.route = route;
    }

    public MWZBounds getBounds() {
        return bounds;
    }

    public void setBounds(MWZBounds bounds) {
        this.bounds = bounds;
    }

    public MWZDirection[] getSubdirections() {
        return subdirections;
    }

    public void setSubdirections(MWZDirection[] subdirections) {
        this.subdirections = subdirections;
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
