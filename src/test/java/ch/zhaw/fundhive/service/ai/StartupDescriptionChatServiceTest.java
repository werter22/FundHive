// src/test/java/ch/zhaw/fundhive/service/ai/StartupDescriptionChatServiceTest.java
package ch.zhaw.fundhive.service.ai;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartupDescriptionChatServiceTest {

    @Mock
    private StartupRepository startupRepository;

    @Mock
    private ChatClient chatClient;

    // These are the fluent API stubs
    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec responseSpec;

    @InjectMocks
    private StartupDescriptionChatService chatService;

    private Startup sample;

    @BeforeEach
    void setUp() {
        // prepare a sample Startup
        sample = new Startup();
        sample.setId("123");
        sample.setName("TestCo");
        sample.setIndustry(IndustryType.TECH);
        sample.setFundingStatus(StartupFundingStatus.SEED);
        sample.setValuation(1_000_000.0);
    }

    @Test
    void improveStartupDescriptionChat_returnsImprovedHtml() {
        // --- arrange ---
        when(startupRepository.findById("123"))
                .thenReturn(Optional.of(sample));

        // stub the ChatClient fluent chain
        // stub the ChatClient fluent chain
        when(chatClient.prompt(anyString()))
                .thenReturn(requestSpec);
        when(requestSpec.user("revise"))
                .thenReturn(requestSpec);

        // create a mock for responseSpec
        when(requestSpec.call())
                .thenReturn(responseSpec);
        when(responseSpec.content())
                .thenReturn("<p>Improved HTML</p>");
        // --- act ---
        String result = chatService.improveStartupDescriptionChat(
                "123",
                "<p>raw</p>",
                "revise");

        // --- assert ---
        assertEquals("<p>Improved HTML</p>", result);

        // verify the repository was queried
        verify(startupRepository).findById("123");

        // capture and inspect the prompt text
        ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
        verify(chatClient).prompt(promptCaptor.capture());
        String prompt = promptCaptor.getValue();

        assertTrue(prompt.contains("Name: TestCo"));
        assertTrue(prompt.contains("Industry: TECH"));
        assertTrue(prompt.contains("Funding: SEED"));
        assertTrue(prompt.contains("Valuation: 1000000.00"));
        assertTrue(prompt.contains("Current description:"));
        assertTrue(prompt.contains("<p>raw</p>"));
        assertTrue(prompt.contains("User request: revise"));

        // verify the rest of the fluent API
        verify(requestSpec).user("revise");
        verify(requestSpec).call();
        verify(responseSpec).content();
    }

    @Test
    void improveStartupDescriptionChat_throws_whenStartupNotFound() {
        // --- arrange ---
        when(startupRepository.findById("notfound"))
                .thenReturn(Optional.empty());

        // --- act & assert ---
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> chatService.improveStartupDescriptionChat(
                        "notfound",
                        "<p>ignored</p>",
                        "anything"));
        assertEquals("Startup not found", ex.getMessage());
    }
}
