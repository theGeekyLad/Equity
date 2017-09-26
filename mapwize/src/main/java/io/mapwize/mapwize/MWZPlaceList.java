package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZPlaceList implements MWZDirectionPoint, MWZSearchable {

    private String identifier;
    private String name;
    private String alias;
    private String venueId;
    private List<String> placeIds;
    private List<MWZTranslation> translations;
    private Map<String, Object> data;

    public MWZPlaceList() {
        super();
    }

    @Override
    public MWZDirectionPointWrapper toDirectionWrapper() {
        MWZDirectionPointWrapper pw = new MWZDirectionPointWrapper();
        pw.setPlaceListId(identifier);
        return pw;
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

    public List<String> getPlaceIds() {
        return placeIds;
    }

    public void setPlaceIds(List<String> placeIds) {
        this.placeIds = placeIds;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public List<MWZTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<MWZTranslation> translations) {
        this.translations = translations;
    }

    public String toString() {
        if (this.placeIds != null) {
            return "ObjectType="+this.getClass()+" Identifier="+this.identifier+" Name="+this.name+" Alias="+this.alias+" VenueId="+this.venueId+" #Places="+this.placeIds.size();
        }
        return "ObjectType="+this.getClass()+" Identifier="+this.identifier+" Name="+this.name+" Alias="+this.alias+" VenueId="+this.venueId+" #Places=0";
    }


}
