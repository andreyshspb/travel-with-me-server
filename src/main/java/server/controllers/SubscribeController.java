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

    @GetMapping("/get_followings/{userID}")
    public List<Long> getFollowings(@PathVariable @NotNull Long userID) {
        return subscribeService.getFollowings(userID);
    }

    @GetMapping("/get_followers/{userID}")
    public List<Long> getFollowers(@PathVariable @NotNull Long userID) {
        return subscribeService.getFollowers(userID);
    }

    @GetMapping("/recommend/{userID}")
    public List<Long> recommend(@PathVariable @NotNull Long userID) {
        return subscribeService.recommend(userID);
    }

}
