package server.controllers;

import com.sun.istack.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.responses.GetUserResponse;
import server.services.SearchService;

import java.util.List;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public List<GetUserResponse> search(@RequestParam @NotNull Long myID,
                                        @RequestParam @NotNull String message,
                                        @RequestParam @NotNull Long offset,
                                        @RequestParam @NotNull Long count) {
        return searchService.search(myID, message, offset, count);
    }
}
