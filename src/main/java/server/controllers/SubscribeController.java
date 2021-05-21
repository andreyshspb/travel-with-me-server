package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.responses.GetUserResponse;
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

    @GetMapping("/get_followings")
    public List<GetUserResponse> getFollowings(@RequestParam @NotNull Long userID,
                                               @RequestParam @NotNull Long offset,
                                               @RequestParam @NotNull Long count) {
        return subscribeService.getFollowings(userID, offset, count);
    }

    @GetMapping("/get_followers")
    public List<GetUserResponse> getFollowers(@RequestParam @NotNull Long userID,
                                              @RequestParam @NotNull Long offset,
                                              @RequestParam @NotNull Long count) {
        return subscribeService.getFollowers(userID, offset, count);
    }

//    @GetMapping("/recommend/{userID}")
//    public List<Long> recommend(@PathVariable @NotNull Long userID) {
//        return subscribeService.recommend(userID);
//    }

}
