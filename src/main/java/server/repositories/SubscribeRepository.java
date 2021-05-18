package server.repositories;

import server.models.Subscribe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends CrudRepository<Subscribe, Long> {

    List<Subscribe> findAllByFollowingId(Long followingId);
    List<Subscribe> findAllByFollowerId(Long followerId);

}
