package io.mapwize.mapwize;

public interface MWZMapViewListener {

    @Deprecated
    void onMapLoad();
    void onReceivedError(String error);
    void onZoomEnd(Integer zoom);
    void onClick(MWZCoordinate latlon);
    void onContextMenu(MWZCoordinate latlon);
    void onFloorChange(Integer floor);
    void onFloorsChange(Integer[] floors);
    void onPlaceClick(MWZPlace place);
    void onVenueClick(MWZVenue venue);
    void onMarkerClick(MWZCoordinate position);
    void onMoveEnd(MWZCoordinate latlon);
    void onUserPositionChange(MWZUserPosition userPosition);
    void onFollowUserModeChange(boolean followUserMode);
    void onDirectionsStart(String info);
    void onDirectionsStop(String info);
    void onMonitoredUuidsChange(String[] uuids);

    void onMissingPermission(String accessFineLocation);
    void onMapLoaded();
}
