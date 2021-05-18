package server.models;

import server.requests.UserEditRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    // unchanged

    private String firstName = null;
    private String lastName = null;
    private String email = null;    // unchanged
    private String avatar = null;    // name file in the amazon s3 database
    private int followersNumber = 0;
    private int followingsNumber = 0;

    public User() {}

    public User(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getFollowersNumber() {
        return followersNumber;
    }

    public int getFollowingsNumber() {
        return followingsNumber;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public User setAll(UserEditRequest user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        return this;
    }

    public User incNumberFollowers() {
        this.followersNumber++;
        return this;
    }

    public User decNumberFollowers() {
        this.followersNumber--;
        return this;
    }

    public User incNumberFollowing() {
        this.followingsNumber++;
        return this;
    }

    public User decNumberFollowing() {
        this.followingsNumber--;
        return this;
    }

}
