package server.services;

import server.models.Subscribe;
import server.repositories.SubscribeRepository;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;
import server.responses.GetUserResponse;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final UserService userService;

    public SubscribeService(SubscribeRepository subscribeRepository, UserService userService) {
        this.subscribeRepository = subscribeRepository;
        this.userService = userService;
    }

    public Subscribe getSubscribe(@NotNull Long followingID, @NotNull Long followerID) {
        for (Subscribe sub : subscribeRepository.findAllByFollowingId(followingID)) {
            if (followerID.equals(sub.getFollowerId())) {
                return sub;
            }
        }
        return null;
    }

    public void addSubscribe(@NotNull Long followingID, @NotNull Long followerID) {
        Subscribe sub = getSubscribe(followingID, followerID);
        if (sub == null) {
            subscribeRepository.save(new Subscribe(followingID, followerID));
            userService.incNumberFollowers(followingID);
            userService.incNumberFollowings(followerID);
        }
    }

    public void deleteSubscribe(@NotNull Long followingID, @NotNull Long followerID) {
        Subscribe sub = getSubscribe(followingID, followerID);
        if (sub != null) {
            subscribeRepository.deleteById(sub.getId());
            userService.decNumberFollowers(followingID);
            userService.decNumberFollowings(followerID);
        }
    }

    public List<GetUserResponse> getFollowings(@NotNull Long userID,
                                               @NotNull Long offset,
                                               @NotNull Long count) {
        return getFollowingsIDs(userID)
                .skip(offset)
                .limit(count)
                .map(userService::getUserById)
                .collect(Collectors.toList());
    }

    public List<GetUserResponse> getFollowers(@NotNull Long userID,
                                              @NotNull Long offset,
                                              @NotNull Long count) {
        return getFollowersIDs(userID)
                .skip(offset)
                .limit(count)
                .map(userService::getUserById)
                .collect(Collectors.toList());
    }

    public List<GetUserResponse> recommend(@NotNull Long userID,
                                           @NotNull Long offset,
                                           @NotNull Long count) {
        Map<Long, Long> frequency = getFollowingsIDs(userID)
                .flatMap(this::getFollowingsIDs)
                .filter(id -> !id.equals(userID))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));
        return frequency.entrySet().stream()
                .sorted(Comparator.comparingLong(entry -> -entry.getValue()))
                .map(Map.Entry::getKey)
                .skip(offset)
                .limit(count)
                .map(userService::getUserById)
                .collect(Collectors.toList());
    }

    public Stream<Long> getFollowingsIDs(@NotNull Long userID) {
        return subscribeRepository.findAllByFollowerId(userID).stream()
                .map(Subscribe::getFollowingId);
    }

    public Stream<Long> getFollowersIDs(@NotNull Long userID) {
        return subscribeRepository.findAllByFollowingId(userID).stream()
                .map(Subscribe::getFollowerId);
    }
}
