package server.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MarkerPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long markerId;
    private String keyName;    // name file in the Amazon S3 database

    public MarkerPhoto() {}

    public MarkerPhoto(Long markerId, String keyName) {
        this.markerId = markerId;
        this.keyName = keyName;
    }

    public Long getId() {
        return id;
    }

    public Long getMarkerId() {
        return markerId;
    }

    public String getKeyName() {
        return keyName;
    }
}
