package server.services;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.models.Chat;
import server.repositories.ChatRepository;
import server.responses.GetUserResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;

    @Autowired
    public ChatService(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;
    }

    public void addChat(@NotNull Long firstID, @NotNull Long secondID) {
        chatRepository.save(new Chat(firstID, secondID));
        chatRepository.save(new Chat(secondID, firstID));
    }

    public void deleteChat(@NotNull Long firstID, @NotNull Long secondID) {
        Long chatA = getChat(firstID, secondID);
        if (chatA != null) {
            chatRepository.deleteById(chatA);
        }
        Long chatB = getChat(secondID, firstID);
        if (chatB != null) {
            chatRepository.deleteById(chatB);
        }
    }

    public List<GetUserResponse> getChats(@NotNull Long userID,
                                          @NotNull Long offset,
                                          @NotNull Long count) {
        return chatRepository.findAllByFromId(userID).stream().
                map(Chat::getToId).
                skip(offset).
                limit(count).
                map(userService::getUserById).
                collect(Collectors.toList());
    }

    private Long getChat(@NotNull Long fromID, @NotNull Long toID) {
        for (Chat chat : chatRepository.findAllByFromId(fromID)) {
            if (toID.equals(chat.getToId())) {
                return chat.getId();
            }
        }
        return null;
    }

}
