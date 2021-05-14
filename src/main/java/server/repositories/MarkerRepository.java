package server.repositories;

import server.models.Marker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkerRepository extends CrudRepository<Marker, Long> {

    List<Marker> findAllByPostId(Long postId);

}
