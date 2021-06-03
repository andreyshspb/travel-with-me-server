package server.services;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.models.PostLike;
import server.repositories.PostLikeRepository;

@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    @Autowired
    public PostLikeService(PostLikeRepository postLikeRepository) {
        this.postLikeRepository = postLikeRepository;
    }

    private Long getLike(@NotNull Long postID, @NotNull Long userID) {
        for (var like : postLikeRepository.findAllByPostId(postID)) {
            if (userID.equals(like.getUserId())) {
                return like.getId();
            }
        }
        return null;
    }

    public void addLike(@NotNull Long postID, @NotNull Long userID) {
        if (getLike(postID, userID) == null) {
            PostLike like = new PostLike(postID, userID);
            postLikeRepository.save(like);
        }
    }

    public void deleteLike(@NotNull Long postID, @NotNull Long userID) {
        Long likeID = getLike(postID, userID);
        if (likeID != null) {
            postLikeRepository.deleteById(likeID);
        }
    }

    public boolean likeExists(@NotNull Long postID, @NotNull Long userID) {
        return getLike(postID, userID) != null;
    }
}
