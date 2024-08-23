package lan.pass.demo.controller;

import lan.pass.demo.model.ChangeMessage;
import lan.pass.demo.request.ChangeMessageRequest;
import lan.pass.demo.service.ChangeMessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/change_message")
public class ChangeMessageController {

    @Resource
    private ChangeMessageService changeMessageService;
    @PostMapping
    public Object createMessage(@RequestBody ChangeMessageRequest request) {
        return changeMessageService.insert(request);
    }

//    @GetMapping("/")
//    public List<ChangeMessage> getAllMessages() {
//
//    }
//
    // Read a single message by ID
    @GetMapping("/{id}")
    public Object getLatest(@PathVariable Long id) {
        return changeMessageService.getLatest(id);
    }

    // Read a single message by ID
    @GetMapping("/history/{id}")
    public Object getHistory(@PathVariable Long id) {
        return changeMessageService.getHistory(id);
    }

    // Read a single message by ID
    @GetMapping("/owner_id/{id}")
    public List<?> getMessagesByOwnerId(@PathVariable Long id,
                                        @RequestParam(value = "beacon", required = false, defaultValue = "true") boolean beacon,
                                        @RequestParam(value = "location", required = false, defaultValue = "true") boolean location,
                                        @RequestParam(value = "field", required = false, defaultValue = "true") boolean field,
                                        @RequestParam(value = "relevantDate", required = false, defaultValue = "true") boolean relevantDate) {
        return changeMessageService.getMessageByOwnerId(id, beacon, location, field, relevantDate);
    }
//
    // Update a message by ID
    @PutMapping
    public void updateMessage(@RequestBody ChangeMessageRequest request) {
        changeMessageService.updateByPrimaryKey(request);
    }
//
//    // Partially update a message by ID (using PATCH)
//    @PatchMapping("/{id}")
//    public void partialUpdateMessage(@PathVariable Long id, @RequestBody ChangeMessageRequest request) {
//
//    }
//
    // Delete a message by ID
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        changeMessageService.deleteByPrimaryKey(id);
    }

    // Delete a message by ID
    @PutMapping("/restore/{id}")
    public Object restoreMessage(@PathVariable Long id) {
        return changeMessageService.restore(id);
    }
}
