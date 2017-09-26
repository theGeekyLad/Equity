package io.mapwize.mapwize;

public class MWZCustomMarkerOptions {

    private String iconUrl;
    private Integer[] iconAnchor;
    private Integer[] iconSize;

    public MWZCustomMarkerOptions(String iconUrl, Integer[] iconAnchor, Integer[] iconSize) {
        if (iconAnchor.length > 2) {
            throw new IllegalArgumentException("iconAnchor lenght should be 2");
        }
        if (iconSize.length > 2) {
            throw new IllegalArgumentException("iconSize lenght should be 2");
        }
        this.iconUrl = iconUrl;
        this.iconAnchor = iconAnchor;
        this.iconSize = iconSize;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public Integer[] getIconAnchor() {
        return iconAnchor;
    }

    public Integer[] getIconSize() {
        return iconSize;
    }

}
