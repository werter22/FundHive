// src/main/java/ch/zhaw/fundhive/controller/ChatController.java
package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.service.ai.StartupSearchChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StartupSearchChatController {

    @Autowired
    private StartupSearchChatService searchChatService;

    @GetMapping("ai/startups/chat")
    public ResponseEntity<String> chat(@RequestParam String message) {
        String answer = searchChatService.handleStartupSearchChat(message);
        return ResponseEntity.ok(answer);
    }
}
