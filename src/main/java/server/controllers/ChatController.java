package server.controllers;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.responses.GetUserResponse;
import server.services.ChatService;

import java.util.List;

@RestController
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/add_chat")
    public void addChat(@RequestParam @NotNull Long firstID,
                        @RequestParam @NotNull Long secondID) {
        chatService.addChat(firstID, secondID);
    }

    @DeleteMapping("/delete_chat")
    public void deleteChat(@RequestParam @NotNull Long firstID,
                           @RequestParam @NotNull Long secondID) {
        chatService.deleteChat(firstID, secondID);
    }

    @GetMapping("/get_chats")
    public List<GetUserResponse> getChats(@RequestParam @NotNull Long userID,
                                          @RequestParam @NotNull Long offset,
                                          @RequestParam @NotNull Long count) {
        return chatService.getChats(userID, offset, count);
    }
}
