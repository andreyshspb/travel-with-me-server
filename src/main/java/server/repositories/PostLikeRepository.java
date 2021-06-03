package server.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import server.models.PostLike;

import java.util.List;

@Repository
public interface PostLikeRepository extends CrudRepository<PostLike, Long> {

    List<PostLike> findAllByPostId(Long postId);

}
