package ch.zhaw.fundhive.service.ai;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.zhaw.fundhive.tools.StartupTools;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ai.chat.client.ChatClient;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StartupSearchChatServiceTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private StartupTools startupTools;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec responseSpec;

    @InjectMocks
    private StartupSearchChatService searchService;

    @Test
    void handleStartupSearchChat_returnsResponseFromAI() {
        // Arrange
        String userMessage = "any biotech startups?";
        String aiResponse = "Try BioGenix, focused on biotech innovation.";

        // Stub ChatClient fluent call chain
        when(chatClient.prompt(anyString())).thenReturn(requestSpec);
        when(requestSpec.tools(startupTools)).thenReturn(requestSpec); // you ARE using `.tools()` in real logic
        when(requestSpec.user(userMessage)).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn(aiResponse);

        // Act
        String result = searchService.handleStartupSearchChat(userMessage);

        // Assert
        assertEquals(aiResponse, result);

        // Optional: verify prompt content
        ArgumentCaptor<String> cap = ArgumentCaptor.forClass(String.class);
        verify(chatClient).prompt(cap.capture());
        String promptText = cap.getValue();

        assertTrue(promptText.contains("Startups you can choose from:"), "Prompt should list startup options");
        assertTrue(promptText.contains("Investor's query"), "Prompt should mention query");

        // Verify method chaining
        verify(requestSpec).tools(startupTools);
        verify(requestSpec).user(userMessage);
        verify(requestSpec).call();
        verify(responseSpec).content();
    }
}