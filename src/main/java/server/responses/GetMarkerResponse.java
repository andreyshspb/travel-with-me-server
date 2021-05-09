package server.responses;

import server.models.Marker;

public class GetMarkerResponse {

    private final String name;
    private final String description;
    private final double latitude;
    private final double longitude;

    public GetMarkerResponse(Marker marker) {
        this.name = marker.getName();
        this.description = marker.getDescription();
        this.latitude = marker.getLatitude();
        this.longitude = marker.getLongitude();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
