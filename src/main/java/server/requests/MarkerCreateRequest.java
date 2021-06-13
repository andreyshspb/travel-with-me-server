package server.requests;

import java.util.List;

public class MarkerCreateRequest {

    private String name;
    private String description;
    private double latitude;
    private double longitude;

    private List<String> photos;

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
