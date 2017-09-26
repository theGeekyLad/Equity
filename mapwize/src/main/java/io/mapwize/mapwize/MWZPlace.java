package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZPlace implements MWZDirectionPoint, MWZSearchable {

    private String identifier;
    private String name;
    private String alias;
    private String venueId;
    private MWZVenue venue;
    private Integer floor;
    private List<MWZTranslation> translations;
    private Integer order;
    private String placeTypeId;
    private Boolean isPublished;
    private Boolean isSearchable;
    private Boolean isVisible;
    private Boolean isClickable;
    private List<String> tags;
    private MWZStyle style;
    private MWZCoordinate marker;
    private MWZCoordinate entrance;
    private MWZGeometry geometry;
    private Map<String, Object> data;

    public MWZPlace(){
        super();
    }

    @Override
    public MWZDirectionPointWrapper toDirectionWrapper() {
        MWZDirectionPointWrapper wrapper = new MWZDirectionPointWrapper();
        wrapper.setPlaceId(identifier);
        return wrapper;
    }

    public MWZVenue getVenue() {
        return venue;
    }

    public void setVenue(MWZVenue venue) {
        this.venue = venue;
    }

    public String getIdentifier() {
        return identifier;
    }

    @JsonSetter("_id")
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public List<MWZTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<MWZTranslation> translations) {
        this.translations = translations;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getPlaceTypeId() {
        return placeTypeId;
    }

    public void setPlaceTypeId(String placeTypeId) {
        this.placeTypeId = placeTypeId;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    @JsonSetter("isPublished")
    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public Boolean getSearchable() {
        return isSearchable;
    }

    @JsonSetter("isSearchable")
    public void setSearchable(Boolean searchable) {
        isSearchable = searchable;
    }

    public Boolean getVisible() {
        return isVisible;
    }

    @JsonSetter("isVisible")
    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public Boolean getClickable() {
        return isClickable;
    }

    @JsonSetter("isClickable")
    public void setClickable(Boolean clickable) {
        isClickable = clickable;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public MWZStyle getStyle() {
        return style;
    }

    public void setStyle(MWZStyle style) {
        this.style = style;
    }

    public MWZCoordinate getMarker() {
        if (this.marker != null) {
            this.marker.setFloor(this.floor);
        }
        return marker;
    }

    public void setMarker(MWZCoordinate marker) {
        this.marker = marker;
    }

    public MWZGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(MWZGeometry geometry) {
        this.geometry = geometry;
    }

    public MWZCoordinate getEntrance() {
        if (this.entrance != null) {
            this.entrance.setFloor(this.floor);
        }
        return entrance;
    }

    public void setEntrance(MWZCoordinate entrance) {
        this.entrance = entrance;
    }

    public MWZBounds getBounds() {
        return geometry.getBounds();
    }

    public String toString() {
        return "ObjectType="+this.getClass()+" Identifier="+this.identifier+" Name="+this.name+" Alias="+this.alias+" VenueId="+this.venueId+" Geometry="+this.geometry.getCoordinates();
    }


}
