package server.repositories;

import server.models.Marker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import server.models.Post;

@Repository
public interface MarkerRepository extends CrudRepository<Marker, Long> {

    Iterable<Marker> findAllByPostId(Long postId);

}
