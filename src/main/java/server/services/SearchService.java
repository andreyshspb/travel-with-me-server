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
    private final ChatService chatService;

    public SearchService(UserService userService, SubscribeService subscribeService,
                         ChatService chatService) {
        this.userService = userService;
        this.subscribeService = subscribeService;
        this.chatService = chatService;
    }

    public List<GetUserResponse> search(@NotNull Long myID,
                                        @NotNull String message,
                                        @NotNull Long offset,
                                        @NotNull Long count) {

        List<Long> suitable = new ArrayList<>();
        for (User user : userService.getAllUsers()) {
            if (isSuitable(user, message)) {
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

    public List<GetUserResponse> searchChats(@NotNull Long myID,
                                             @NotNull String message,
                                             @NotNull Long offset,
                                             @NotNull Long count) {

        List<Long> suitable = new ArrayList<>();
        for (Long userID : chatService.getChatsIDs(myID)) {
            User user = userService.getUser(userID);
            if (isSuitable(user, message)) {
                suitable.add(user.getId());
            }
        }

        return suitable.stream()
                .skip(offset)
                .limit(count)
                .map(userService::getUserById)
                .collect(Collectors.toList());
    }

    private boolean isSuitable(@NotNull User user, @NotNull String message) {
        message = message.toLowerCase();
        String pattern1 = user.getFirstName() + user.getLastName();
        pattern1 = pattern1.toLowerCase();
        String pattern2 = user.getLastName() + user.getFirstName();
        pattern2 = pattern2.toLowerCase();
        return pattern1.contains(message) || pattern2.contains(message);
    }

}
