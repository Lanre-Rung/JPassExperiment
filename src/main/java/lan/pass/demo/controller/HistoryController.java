package lan.pass.demo.controller;

import lan.pass.demo.model.History;
import lan.pass.demo.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private HistoryService historyService;

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    // GET request to search history by user ID or pass ID
    @GetMapping
    public List<History> searchHistory(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "passId", required = false) Long passId) {
        return historyService.searchHistory(userId, passId);
    }
}