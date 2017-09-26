package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MWZGeometryPolygon.class, name = "Polygon"),

        @JsonSubTypes.Type(value = MWZGeometryPoint.class, name = "Point") }
)
public abstract class MWZGeometry {

    public Object getCoordinates(){
        return null;
    }

    public void setCoordinates(Object coordinates) {}

    public MWZBounds getBounds() {
        return null;
    }
}
