package server.services;

import server.models.User;
import server.repositories.UserRepository;
import server.requests.UserEditRequest;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;
import server.responses.GetUserResponse;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StorageService storageService;

    public UserService(UserRepository userRepository, StorageService storageService) {
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    public GetUserResponse getUserById(@NotNull Long userID) {
        User user = userRepository.findById(userID).orElse(null);
        if (user != null) {
            String avatar = storageService.downloadFile(user.getAvatar());
            return new GetUserResponse(user, avatar);
        }
        return null;
    }

    public GetUserResponse getUser(@NotNull String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            String avatar = null;
            if (user.getAvatar() != null) {
                avatar = storageService.downloadFile(user.getAvatar());
            }
            return new GetUserResponse(user, avatar);
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

    public void editAvatar(@NotNull Long userID, @NotNull String newAvatar) {
        // name avatar in the amazon s3 database
        String avatarName = "avatar_" + userID;

        // add changes to amazon s3 database
        storageService.removeFile(avatarName);
        storageService.uploadFile(avatarName, newAvatar);

        // add changes to mysql database
        Optional<User> user = userRepository.findById(userID);
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
