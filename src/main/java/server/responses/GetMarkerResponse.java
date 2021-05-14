package server.responses;

import server.models.Marker;

import java.util.List;

public class GetMarkerResponse {

    private final String name;
    private final String description;
    private final double latitude;
    private final double longitude;

    private final List<String> photos;

    public GetMarkerResponse(Marker marker, List<String> photos) {
        this.name = marker.getName();
        this.description = marker.getDescription();
        this.latitude = marker.getLatitude();
        this.longitude = marker.getLongitude();
        this.photos = photos;
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

    public List<String> getPhotos() {
        return photos;
    }
}
