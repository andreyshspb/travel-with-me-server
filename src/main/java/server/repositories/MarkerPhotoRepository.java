package server.repositories;

import server.models.MarkerPhoto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarkerPhotoRepository extends CrudRepository<MarkerPhoto, Long> {

    List<MarkerPhoto> findAllByMarkerId(Long markerId);

}
