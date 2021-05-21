package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.services.SubscribeService;
import com.sun.istack.NotNull;

import java.util.List;

@RestController
public class SubscribeController {

    private final SubscribeService subscribeService;

    @Autowired
    public SubscribeController(SubscribeService subscribeService) {
        this.subscribeService = subscribeService;
    }

    @PutMapping("/add_subscribe")
    public void addSubscribe(@RequestParam @NotNull Long followingID,
                             @RequestParam @NotNull Long followerID) {
        subscribeService.addSubscribe(followingID, followerID);
    }

    @DeleteMapping("/delete_subscribe")
    public void deleteSubscribe(@RequestParam @NotNull Long followingID,
                                @RequestParam @NotNull Long followerID) {
        subscribeService.deleteSubscribe(followingID, followerID);
    }

    @GetMapping("/get_followings/{userId}")
    public List<Long> getFollowings(@PathVariable @NotNull Long userId) {
        return subscribeService.getFollowings(userId);
    }

    @GetMapping("/get_followers/{userId}")
    public List<Long> getFollowers(@PathVariable @NotNull Long userId) {
        return subscribeService.getFollowers(userId);
    }

    @GetMapping("/recommend/{userID}")
    public List<Long> recommend(@PathVariable @NotNull Long userID) {
        return subscribeService.recommend(userID);
    }

}
