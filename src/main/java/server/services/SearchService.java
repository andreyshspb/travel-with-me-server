package server.services;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;
import server.models.User;
import server.responses.GetUserResponse;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final UserService userService;
    private final SubscribeService subscribeService;

    public SearchService(UserService userService, SubscribeService subscribeService) {
        this.userService = userService;
        this.subscribeService = subscribeService;
    }

    public List<GetUserResponse> search(@NotNull Long myID,
                                        @NotNull String message,
                                        @NotNull Long offset,
                                        @NotNull Long count) {
        message = message.toLowerCase();

        List<Long> suitable = new ArrayList<>();
        for (User user : userService.getAllUsers()) {
            String pattern1 = user.getFirstName() + user.getLastName();
            pattern1 = pattern1.toLowerCase();
            String pattern2 = user.getLastName() + user.getFirstName();
            pattern2 = pattern2.toLowerCase();
            if (pattern1.contains(message) || pattern2.contains(message)) {
                suitable.add(user.getId());
            }
        }

        Deque<Long> result = new LinkedList<>();
        for (Long id : suitable) {
            if (subscribeService.existingSubscribe(id, myID)) {
                result.addFirst(id);
            } else {
                result.addLast(id);
            }
        }

        return result.stream()
                .skip(offset)
                .limit(count)
                .map(userService::getUserById)
                .collect(Collectors.toList());
    }

}
