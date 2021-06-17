package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import server.models.Marker;
import server.models.MarkerPhoto;
import server.models.Post;
import server.repositories.MarkerPhotoRepository;
import server.repositories.MarkerRepository;
import server.repositories.PostRepository;
import server.requests.MarkerCreateRequest;
import server.requests.PostCreateRequest;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;
import server.responses.GetMarkerResponse;
import server.responses.GetPostResponse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MarkerRepository markerRepository;
    private final MarkerPhotoRepository markerPhotoRepository;
    private final StorageService storageService;
    private final SubscribeService subscribeService;
    private final PostLikeService postLikeService;

    @Autowired
    public PostService(PostRepository postRepository, MarkerRepository markerRepository,
                       MarkerPhotoRepository markerPhotoRepository, StorageService storageService,
                       SubscribeService subscribeService, PostLikeService postLikeService) {
        this.postRepository = postRepository;
        this.markerRepository = markerRepository;
        this.markerPhotoRepository = markerPhotoRepository;
        this.storageService = storageService;
        this.subscribeService = subscribeService;
        this.postLikeService = postLikeService;
    }

    public Long addPost(@NotNull PostCreateRequest postCreateRequest) {
        String pictureName = null;
        if (postCreateRequest.getPicture() != null) {
            // name in the amazon s3 database
            pictureName = "post_" + postCreateRequest.getAuthorId() +
                    "_" + System.currentTimeMillis();

            // add picture to amazon s3 database
            storageService.uploadFile(pictureName, postCreateRequest.getPicture());
        }

        // add post to mysql database
        Post post = new Post(postCreateRequest, pictureName);
        postRepository.save(post);

        // add marker to mysql
        addMarkers(postCreateRequest.getMarkers(), post.getId());

        return post.getId();
    }

    private GetPostResponse getPost(@NotNull Long postID) {
        Optional<Post> maybePost = postRepository.findById(postID);
        if (maybePost.isPresent()) {
            Post post = maybePost.get();
            return new GetPostResponse(post, getMarkers(post.getId()));
        }
        return null;
    }

    private Stream<Long> getPostsIDs(@NotNull Long authorID) {
        return postRepository.findAllByAuthorId(authorID).stream()
                .map(Post::getId);
    }

    public List<GetPostResponse> getPosts(@NotNull Long authorID,
                                          @NotNull Long offset,
                                          @NotNull Long count) {
        return getPostsIDs(authorID)
                .sorted(Comparator.reverseOrder())
                .skip(offset)
                .limit(count)
                .map(this::getPost)
                .collect(Collectors.toList());
    }

    public List<GetPostResponse> getFollowingsPost(@NotNull Long userID,
                                                   @NotNull Long offset,
                                                   @NotNull Long count) {
        return subscribeService.getFollowingsIDs(userID)
                .flatMap(this::getPostsIDs)
                .sorted(Comparator.reverseOrder())
                .skip(offset)
                .limit(count)
                .map(this::getPost)
                .collect(Collectors.toList());
    }

    public void editDescription(@NotNull Long postID, @NotNull String newDescription) {
        Optional<Post> post = postRepository.findById(postID);
        post.ifPresent(value -> postRepository.save(value.setDescription(newDescription)));
    }

    public void incNumberLikes(@NotNull Long postID, @NotNull Long userID) {
        Optional<Post> post = postRepository.findById(postID);
        post.ifPresent(value -> postRepository.save(value.incNumberLikes()));
        postLikeService.addLike(postID, userID);
    }

    public void decNumberLikes(@NotNull Long postID, @NotNull Long userID) {
        Optional<Post> post = postRepository.findById(postID);
        post.ifPresent(value -> postRepository.save(value.decNumberLikes()));
        postLikeService.deleteLike(postID, userID);
    }

    public boolean likeExists(@NotNull Long postID, @NotNull Long userID) {
        return postLikeService.likeExists(postID, userID);
    }

    public void deletePost(@NotNull Long postID) {
        postRepository.deleteById(postID);
    }


    private void addMarkers(Iterable<MarkerCreateRequest> markers, Long postID) {
        int index = 0;
        for (var markerRequest : markers) {
            Marker marker = new Marker(markerRequest, postID, index);
            markerRepository.save(marker);
            for (String photo : markerRequest.getPhotos()) {
                String pictureName = marker.getId().toString() + "_" + Integer.valueOf(index).toString();
                storageService.uploadFile(pictureName, photo);
                markerPhotoRepository.save(new MarkerPhoto(marker.getId(), pictureName));
            }
            index += 1;
        }
    }

    private List<GetMarkerResponse> getMarkers(Long postID) {
        List<GetMarkerResponse> markers = new ArrayList<>();
        for (Marker marker : markerRepository.findAllByPostId(postID)) {
            List<String> photos = new ArrayList<>();
            for (MarkerPhoto photo : markerPhotoRepository.findAllByMarkerId(marker.getId())) {
                photos.add(photo.getKeyName());
            }
            markers.add(new GetMarkerResponse(marker, photos));
        }
        return markers;
    }


}
