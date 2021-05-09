package server.responses;

import server.models.Post;

import java.util.List;

public class GetPostResponse {

    private final Long postId;
    private final Long authorId;
    private final String date;
    private final String description;
    private final String pictureName;
    private final int numberLikes;

    private final List<GetMarkerResponse> markers;

    public GetPostResponse(Post post, List<GetMarkerResponse> markers) {
        this.postId = post.getId();
        this.authorId = post.getAuthorId();
        this.date = post.getDate();
        this.description = post.getDescription();
        this.pictureName = post.getPictureName();
        this.numberLikes = post.getNumberLikes();
        this.markers = markers;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureName() {
        return pictureName;
    }

    public int getNumberLikes() {
        return numberLikes;
    }

    public List<GetMarkerResponse> getMarkers() {
        return markers;
    }
}
