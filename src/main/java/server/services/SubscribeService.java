package server.services;

import server.models.Subscribe;
import server.models.User;
import server.repositories.SubscribeRepository;
import server.requests.SubscribeRequest;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final UserService userService;

    public SubscribeService(SubscribeRepository subscribeRepository, UserService userService) {
        this.subscribeRepository = subscribeRepository;
        this.userService = userService;
    }

    public void addSubscribe(@NotNull SubscribeRequest subscribeRequest) {
        subscribeRepository.save(new Subscribe(subscribeRequest));
        userService.incNumberFollowers(subscribeRequest.getFollowingId());
        userService.incNumberFollowings(subscribeRequest.getFollowerId());
    }

    public void deleteSubscribe(@NotNull SubscribeRequest subscribeRequest) {
        subscribeRepository.delete(new Subscribe(subscribeRequest));
        userService.decNumberFollowers(subscribeRequest.getFollowingId());
        userService.decNumberFollowings(subscribeRequest.getFollowerId());
    }

    public List<User> getFollowings(Long userId) {
        Iterable<Subscribe> buffer = subscribeRepository.findAllByFollowerId(userId);
        List<User> result = new ArrayList<>();
        for (var follower : buffer) {
            result.add(userService.getUserById(follower.getFollowingId()));
        }
        return result;
    }

    public List<User> getFollowers(Long userId) {
        Iterable<Subscribe> buffer = subscribeRepository.findAllByFollowerId(userId);
        List<User> result = new ArrayList<>();
        for (var follower : buffer) {
            result.add(userService.getUserById(follower.getFollowerId()));
        }
        return result;
    }
}
