package io.mapwize.mapwize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MWZStyle {

    private String markerUrl;
    private Boolean markerDisplay;
    private String strokeColor;
    private Double strokeOpacity;
    private Integer strokeWidth;
    private String fillColor;
    private Double fillOpacity;
    private String labelBackgroundColor;
    private Double labelBackgroundOpacity;

    public MWZStyle() {
        super();
    }

    public String getMarkerUrl() {
        return markerUrl;
    }

    public void setMarkerUrl(String markerUrl) {
        this.markerUrl = markerUrl;
    }

    public Boolean getMarkerDisplay() {
        return markerDisplay;
    }

    public void setMarkerDisplay(Boolean markerDisplay) {
        this.markerDisplay = markerDisplay;
    }

    public String getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
    }

    public Double getStrokeOpacity() {
        return strokeOpacity;
    }

    public void setStrokeOpacity(Double strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    public Integer getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(Integer strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public Double getFillOpacity() {
        return fillOpacity;
    }

    public void setFillOpacity(Double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }

    public String getLabelBackgroundColor() {
        return labelBackgroundColor;
    }

    public void setLabelBackgroundColor(String labelBackgroundColor) {
        this.labelBackgroundColor = labelBackgroundColor;
    }

    public Double getLabelBackgroundOpacity() {
        return labelBackgroundOpacity;
    }

    public void setLabelBackgroundOpacity(Double labelBackgroundOpacity) {
        this.labelBackgroundOpacity = labelBackgroundOpacity;
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
