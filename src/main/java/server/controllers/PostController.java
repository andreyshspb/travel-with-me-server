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

    @GetMapping("/get_posts")
    public List<GetPostResponse> getPosts(@RequestParam @NotNull Long authorID,
                                          @RequestParam @NotNull Long offset,
                                          @RequestParam @NotNull Long count) {
        return postService.getPosts(authorID, offset, count);
    }

//    @GetMapping("/get_followings_posts")
//    public List<GetPostResponse> getFollowingsPosts(@RequestParam @NotNull Long userID,
//                                                    @RequestParam @NotNull Long offset,
//                                                    @RequestParam @NotNull Long count) {
//        return postService.getFollowingsPost(userID, offset, count);
//    }

    @PutMapping("/edit_description")
    public void editDescription(@RequestParam @NotNull Long postID,
                                @RequestParam @NotNull String newDescription) {
        postService.editDescription(postID, newDescription);
    }

    @PostMapping("/inc_post_number_likes/{postID}")
    public void incNumberLikes(@PathVariable @NotNull Long postID) {
        postService.incNumberLikes(postID);
    }

    @PostMapping("/dec_post_number_likes/{postID}")
    public void decNumberLikes(@PathVariable @NotNull Long postID) {
        postService.decNumberLikes(postID);
    }

    @DeleteMapping("/delete_post/{postID}")
    public void deletePost(@PathVariable @NotNull Long postID) {
        postService.deletePost(postID);
    }

}
