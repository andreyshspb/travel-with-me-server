package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import server.models.User;
import server.repositories.UserRepository;
import server.requests.AvatarEditRequest;
import server.requests.UserEditRequest;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;
import server.responses.GetUserResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscribeService subscribeService;
    private final StorageService storageService;

    @Autowired
    public UserService(UserRepository userRepository, SubscribeService subscribeService,
                       StorageService storageService) {
        this.userRepository = userRepository;
        this.subscribeService = subscribeService;
        this.storageService = storageService;
    }

    public GetUserResponse getUserById(@NotNull Long userID) {
        User user = userRepository.findById(userID).orElse(null);
        if (user != null) {
            String avatar = null;
            if (user.getAvatar() != null) {
                avatar = storageService.downloadFile(user.getAvatar());
            }
            return new GetUserResponse(user, avatar);
        }
        return null;
    }

    public GetUserResponse getUser(@NotNull String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return getUserById(user.getId());
        }
        return null;
    }

    public void addUser(@NotNull String email) {
        Optional<User> maybeUser = userRepository.findByEmail(email);
        if (maybeUser.isEmpty()) {
            userRepository.save(new User(email));
        }
    }

    public void editUser(@NotNull UserEditRequest editedUser) {
        Optional<User> user = userRepository.findById(editedUser.getUserId());
        user.ifPresent(value -> userRepository.save(value.setAll(editedUser)));
    }

    public void editAvatar(@NotNull AvatarEditRequest avatarEditRequest) {
        // name avatar in the amazon s3 database
        String avatarName = "avatar_" + avatarEditRequest.getUserId();

        // add changes to amazon s3 database
        storageService.removeFile(avatarName);
        storageService.uploadFile(avatarName, avatarEditRequest.getAvatar());

        // add changes to mysql database
        Optional<User> user = userRepository.findById(avatarEditRequest.getUserId());
        user.ifPresent(value -> userRepository.save(value.setAvatar(avatarName)));
    }

    public void incNumberFollowers(@NotNull Long userID) {
        Optional<User> user = userRepository.findById(userID);
        user.ifPresent(value -> userRepository.save(value.incNumberFollowers()));
    }

    public void decNumberFollowers(@NotNull Long userID) {
        Optional<User> user = userRepository.findById(userID);
        user.ifPresent(value -> userRepository.save(value.decNumberFollowers()));
    }

    public void incNumberFollowings(@NotNull Long userID) {
        Optional<User> user = userRepository.findById(userID);
        user.ifPresent(value -> userRepository.save(value.incNumberFollowing()));
    }

    public void decNumberFollowings(@NotNull Long userID) {
        Optional<User> user = userRepository.findById(userID);
        user.ifPresent(value -> userRepository.save(value.decNumberFollowing()));
    }

    public List<GetUserResponse> search(@NotNull Long myID,
                                        @NotNull String message,
                                        @NotNull Long offset,
                                        @NotNull Long count) {
        message = message.toLowerCase();

        List<Long> suitable = new ArrayList<>();
        for (User user : userRepository.findAll()) {
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
                .map(this::getUserById)
                .collect(Collectors.toList());
    }
}
