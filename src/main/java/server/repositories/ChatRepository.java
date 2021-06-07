package server.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import server.models.Chat;

import java.util.List;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {

    List<Chat> findAllByFromId(Long fromId);

}
