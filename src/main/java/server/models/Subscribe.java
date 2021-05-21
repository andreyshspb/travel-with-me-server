package server.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long followingId;
    private Long followerId;

    public Subscribe() {}

    public Subscribe(Long followingId, Long followerId) {
        this.followingId = followingId;
        this.followerId = followerId;
    }

    public Long getId() {
        return id;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public Long getFollowerId() {
        return followerId;
    }
}
