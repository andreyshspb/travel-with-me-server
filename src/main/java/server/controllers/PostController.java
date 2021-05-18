package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.requests.PostCreateRequest;
import server.responses.GetPostResponse;
import server.services.PostService;
import com.sun.istack.NotNull;

import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/add_post")
    public void addPost(@RequestBody @NotNull PostCreateRequest postCreateRequest) {
        postService.addPost(postCreateRequest);
    }

    @GetMapping("/get_post/{postId}")
    public GetPostResponse getPost(@PathVariable @NotNull Long postId) {
        return postService.getPost(postId);
    }

    @GetMapping("/get_posts/{authorId}")
    public List<Long> getPosts(@PathVariable @NotNull Long authorId) {
        return postService.getPosts(authorId);
    }

    @GetMapping("/get_followings_posts/{userID}")
    public List<Long> getFollowingsPosts(@PathVariable @NotNull Long userID) {
        return postService.getFollowingsPost(userID);
    }

    @PutMapping("/edit_description")
    public void editDescription(@RequestParam @NotNull Long postId,
                                @RequestParam @NotNull String newDescription) {
        postService.editDescription(postId, newDescription);
    }

    @PostMapping("/inc_post_number_likes/{postId}")
    public void incNumberLikes(@PathVariable @NotNull Long postId) {
        postService.incNumberLikes(postId);
    }

    @PostMapping("/dec_post_number_likes/{postId}")
    public void decNumberLikes(@PathVariable @NotNull Long postId) {
        postService.decNumberLikes(postId);
    }

    @DeleteMapping("/delete_post/{postId}")
    public void deletePost(@PathVariable @NotNull Long postId) {
        postService.deletePost(postId);
    }

}
