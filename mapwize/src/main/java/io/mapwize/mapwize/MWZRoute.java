package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZRoute {

    private Integer floor;
    private Integer fromFloor;
    private Integer toFloor;
    private Boolean isStart;
    private Boolean isEnd;
    private Double traveltime;
    private Double timeToEnd;
    private MWZBounds bounds;
    private Double distance;
    private String connectorTypeTo;
    private String connectorTypeFrom;
    private List<Object> path;

    public MWZRoute() {
        super();
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

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getFromFloor() {
        return fromFloor;
    }

    public void setFromFloor(Integer fromFloor) {
        this.fromFloor = fromFloor;
    }

    public Integer getToFloor() {
        return toFloor;
    }

    public void setToFloor(Integer toFloor) {
        this.toFloor = toFloor;
    }

    public Boolean getStart() {
        return isStart;
    }

    public void setStart(Boolean start) {
        isStart = start;
    }

    public Boolean getEnd() {
        return isEnd;
    }

    public void setEnd(Boolean end) {
        isEnd = end;
    }

    public Double getTraveltime() {
        return traveltime;
    }

    public void setTraveltime(Double traveltime) {
        this.traveltime = traveltime;
    }

    public Double getTimeToEnd() {
        return timeToEnd;
    }

    public void setTimeToEnd(Double timeToEnd) {
        this.timeToEnd = timeToEnd;
    }

    public MWZBounds getBounds() {
        return bounds;
    }

    public void setBounds(MWZBounds bounds) {
        this.bounds = bounds;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getConnectorTypeTo() {
        return connectorTypeTo;
    }

    public void setConnectorTypeTo(String connectorTypeTo) {
        this.connectorTypeTo = connectorTypeTo;
    }

    public String getConnectorTypeFrom() {
        return connectorTypeFrom;
    }

    public void setConnectorTypeFrom(String connectorTypeFrom) {
        this.connectorTypeFrom = connectorTypeFrom;
    }

    public List<Object> getPath() {
        return path;
    }

    public void setPath(List<Object> path) {
        this.path = path;
    }
}
