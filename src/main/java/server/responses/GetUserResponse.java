package server.responses;

import server.models.User;

public class GetUserResponse {

    private final Long userID;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String avatar;
    private final int followersNumber;
    private final int followingsNumber;

    public GetUserResponse(User user, String avatar) {
        this.userID = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.avatar = avatar;
        this.followersNumber = user.getFollowersNumber();
        this.followingsNumber = user.getFollowingsNumber();
    }

    public Long getUserID() {
        return userID;
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
}
