package server.services;

import org.apache.logging.log4j.util.PropertySource;
import server.models.Subscribe;
import server.models.User;
import server.repositories.SubscribeRepository;
import server.requests.SubscribeRequest;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;
import server.responses.GetUserResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public List<Long> getFollowings(Long userID) {
        return subscribeRepository.findAllByFollowerId(userID).stream()
                .map(Subscribe::getFollowingId)
                .collect(Collectors.toList());
    }

    public List<Long> getFollowers(Long userID) {
        return subscribeRepository.findAllByFollowingId(userID).stream()
                .map(Subscribe::getFollowingId)
                .collect(Collectors.toList());
    }

    public List<Long> recommend(Long userID) {
        Map<Long, Long> frequency = getFollowings(userID).stream()
                .flatMap(id -> getFollowings(id).stream())
                .filter(id -> !id.equals(userID))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        return frequency.entrySet().stream()
                .sorted(Comparator.comparingLong(entry -> -entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
