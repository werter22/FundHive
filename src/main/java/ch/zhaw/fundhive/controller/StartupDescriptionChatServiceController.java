package ch.zhaw.fundhive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.fundhive.service.ai.StartupDescriptionChatService;
import ch.zhaw.fundhive.service.helpers.OwnershipService;
import ch.zhaw.fundhive.service.helpers.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api")
public class StartupDescriptionChatServiceController {

    @Autowired
    private UserService userService;

    @Autowired
    private OwnershipService ownerService;

    @Autowired
    private StartupDescriptionChatService chatService;

    /* --- Get AI suggestions for description improvments --- */

    @PostMapping("/startups/{id}/chat")
    public ResponseEntity<String> improveDescription(
            @PathVariable String id,
            @RequestBody String htmlContent,
            @RequestParam String userInput) {

        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userId = userService.getCurrentUserId();
        if (!ownerService.ownsStartup(id, userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String suggestion = chatService.improveStartupDescriptionChat(
                id, htmlContent, userInput);
        return new ResponseEntity<>(suggestion, HttpStatus.OK);
    }

}
