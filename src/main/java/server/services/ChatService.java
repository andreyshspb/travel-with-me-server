package server.services;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.models.Chat;
import server.repositories.ChatRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public void addChat(@NotNull Long firstID, @NotNull Long secondID) {
        chatRepository.save(new Chat(firstID, secondID));
        chatRepository.save(new Chat(secondID, firstID));
    }

    public List<Long> getChats(@NotNull Long userID,
                               @NotNull Long offset,
                               @NotNull Long count) {
        return chatRepository.findAllByFromId(userID).stream().
                map(Chat::getToId).
                skip(offset).
                limit(count).
                collect(Collectors.toList());
    }

}
