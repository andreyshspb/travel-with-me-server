package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import server.requests.AvatarEditRequest;
import server.requests.UserEditRequest;
import server.responses.GetUserResponse;
import server.services.UserService;
import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get_user")
    public GetUserResponse getUser(@RequestParam @NotNull String email) {
        return userService.getUser(email);
    }

    @GetMapping("/get_user/{userID}")
    public GetUserResponse getUserByID(@PathVariable @NotNull Long userID) {
        return userService.getUserById(userID);
    }

    @PostMapping("/add_user")
    public void addUser(@RequestParam @NotNull String email) {
        userService.addUser(email);
    }

    @PutMapping("/edit_user")
    public void editUser(@RequestBody @NotNull UserEditRequest editedUser) {
        userService.editUser(editedUser);
    }

    @PutMapping("/edit_avatar")
    public void editAvatar(@RequestBody @NotNull AvatarEditRequest avatarEditRequest) {
        userService.editAvatar(avatarEditRequest);
    }

    @PostMapping("/inc_number_followers/{userID}")
    public void incNumberFollowers(@PathVariable @NotNull Long userID) {
        userService.incNumberFollowers(userID);
    }

    @PostMapping("/dec_number_followers/{userID}")
    public void decNumberFollowers(@PathVariable @NotNull Long userID) {
        userService.decNumberFollowers(userID);
    }

    @PostMapping("/inc_number_followings/{userID}")
    public void incNumberFollowings(@PathVariable @NotNull Long userID) {
        userService.incNumberFollowings(userID);
    }

    @PostMapping("/dec_number_followings/{userID}")
    public void decNumberFollowings(@PathVariable @NotNull Long userID) {
        userService.decNumberFollowings(userID);
    }

}
