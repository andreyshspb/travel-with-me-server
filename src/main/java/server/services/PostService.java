package server.services;

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

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MarkerRepository markerRepository;
    private final MarkerPhotoRepository markerPhotoRepository;
    private final StorageService storageService;

    public PostService(PostRepository postRepository, MarkerRepository markerRepository,
                       MarkerPhotoRepository markerPhotoRepository, StorageService storageService) {
        this.postRepository = postRepository;
        this.markerRepository = markerRepository;
        this.markerPhotoRepository = markerPhotoRepository;
        this.storageService = storageService;
    }

    public void addPost(@NotNull PostCreateRequest postCreateRequest) {
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
    }

    public GetPostResponse getPost(@NotNull Long postId) {
        Optional<Post> maybePost = postRepository.findById(postId);
        if (maybePost.isPresent()) {
            Post post = maybePost.get();
            String picture = null;
            if (post.getPictureName() != null) {
                picture = storageService.downloadFile(post.getPictureName());
            }
            return new GetPostResponse(post, getMarkers(post.getId()), picture);
        }
        return null;
    }

    public List<GetPostResponse> getPosts(@NotNull Long authorId) {
        return getPostsIDs(authorId).stream()
                .map(this::getPost)
                .collect(Collectors.toList());
    }

    public List<Long> getPostsIDs(@NotNull Long authorId) {
        return postRepository.findAllByAuthorId(authorId).stream()
                .mapToLong(Post::getId)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public void editDescription(@NotNull Long postId, @NotNull String newDescription) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(value -> postRepository.save(value.setDescription(newDescription)));
    }

    public void incNumberLikes(@NotNull Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(value -> postRepository.save(value.incNumberLikes()));
    }

    public void decNumberLikes(@NotNull Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(value -> postRepository.save(value.decNumberLikes()));
    }

    public void deletePost(@NotNull Long postId) {
        postRepository.deleteById(postId);
    }


    private void addMarkers(Iterable<MarkerCreateRequest> markers, Long postId) {
        int index = 0;
        for (var markerRequest : markers) {
            Marker marker = new Marker(markerRequest, postId, index);
            markerRepository.save(marker);
            for (var photo : markerRequest.getPhotos()) {
                String pictureName = marker.getId().toString() + "_" + Integer.valueOf(index).toString();
                storageService.uploadFile(pictureName, photo.getPhoto());
                markerPhotoRepository.save(new MarkerPhoto(marker.getId(), pictureName));
            }
            index += 1;
        }
    }

    private List<GetMarkerResponse> getMarkers(Long postId) {
        List<GetMarkerResponse> markers = new ArrayList<>();
        for (Marker marker : markerRepository.findAllByPostId(postId)) {
            List<String> photos = new ArrayList<>();
            for (MarkerPhoto photo : markerPhotoRepository.findAllByMarkerId(marker.getId())) {
                String picture= storageService.downloadFile(photo.getKeyName());
                photos.add(picture);
            }
            markers.add(new GetMarkerResponse(marker, photos));
        }
        return markers;
    }


}
