package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import io.mapwize.mapwize.MWZDirectionOptions;
import io.mapwize.mapwize.MWZDirectionPointWrapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MWZDirectionRequestObject {

    MWZDirectionPointWrapper from;
    List<MWZDirectionPointWrapper> to;
    List<MWZDirectionPointWrapper> waypoints;
    MWZDirectionOptions options;

    public MWZDirectionRequestObject() {
        super();
    }

    public MWZDirectionPointWrapper getFrom() {
        return from;
    }

    public void setFrom(MWZDirectionPointWrapper from) {
        this.from = from;
    }

    public List<MWZDirectionPointWrapper> getTo() {
        return to;
    }

    public void setTo(List<MWZDirectionPointWrapper> to) {
        this.to = to;
    }

    public List<MWZDirectionPointWrapper> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<MWZDirectionPointWrapper> waypoints) {
        this.waypoints = waypoints;
    }

    public MWZDirectionOptions getOptions() {
        return options;
    }

    public void setOptions(MWZDirectionOptions options) {
        this.options = options;
    }
}
