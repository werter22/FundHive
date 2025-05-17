package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.service.ai.StartupDescriptionChatService;
import ch.zhaw.fundhive.service.helpers.OwnershipService;
import ch.zhaw.fundhive.service.helpers.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StartupDescriptionChatServiceController.class)
class StartupDescriptionChatServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OwnershipService ownerService;

    @MockBean
    private StartupDescriptionChatService chatService;

    @Test
    void chat_forbidden_whenNotEntrepreneur() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(false);

        mockMvc.perform(post("/api/startups/XYZ/chat")
                .with(jwt()) // Needed to populate SecurityContextHolder
                .param("userInput", "tips")
                .contentType(MediaType.TEXT_PLAIN)
                .content("<p>hey</p>"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(ownerService, chatService);
    }

    @Test
    void chat_forbidden_whenNotOwner() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        when(ownerService.ownsStartup("XYZ", "user1")).thenReturn(false);

        mockMvc.perform(post("/api/startups/XYZ/chat")
                .with(jwt().jwt(jwt -> jwt
                        .claim("user_roles", List.of("entrepreneur"))
                        .subject("user1")))
                .param("userInput", "tips")
                .contentType(MediaType.TEXT_PLAIN)
                .content("<p>hey</p>"))
                .andExpect(status().isForbidden());

        verify(chatService, never()).improveStartupDescriptionChat(any(), any(), any());
    }

    @Test
    void chat_returnsSuggestion_whenAllowed() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        when(ownerService.ownsStartup("XYZ", "user1")).thenReturn(true);
        when(chatService.improveStartupDescriptionChat(
                eq("XYZ"),
                anyString(),
                eq("improve"))).thenReturn("<p>new</p>");

        mockMvc.perform(post("/api/startups/XYZ/chat")
                .with(jwt().jwt(jwt -> jwt
                        .claim("user_roles", List.of("entrepreneur"))
                        .subject("user1")))
                .param("userInput", "improve")
                .contentType(MediaType.TEXT_PLAIN)
                .content("<p>old</p>"))
                .andExpect(status().isOk());
    }
}
