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


@Service
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    @Autowired
    public UserService(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(@NotNull Long userID) {
        return userRepository.findById(userID).orElse(null);
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
}
