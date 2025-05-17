package ch.zhaw.fundhive.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import ch.zhaw.fundhive.service.ai.StartupSearchChatService;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

@WebMvcTest(StartupSearchChatController.class)
class StartupSearchChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StartupSearchChatService searchChatService;

    static Stream<TestCase> userRoles() {
        return Stream.of(
                new TestCase("unauthenticated", null, 401), // no JWT at all
                new TestCase("entrepreneur", jwt().jwt(jwt -> jwt.claim("user_roles", List.of("entrepreneur"))), 200),
                new TestCase("admin", jwt().jwt(jwt -> jwt.claim("user_roles", List.of("admin"))), 200),
                new TestCase("investor", jwt().jwt(jwt -> jwt.claim("user_roles", List.of("investor"))), 200));
    }

    @ParameterizedTest
    @MethodSource("userRoles")
    void testChatAccessByRole(TestCase tc) throws Exception {
        String input = "any query";
        String reply = "mock reply";

        // stub the service
        when(searchChatService.handleStartupSearchChat(input)).thenReturn(reply);

        var req = get("/api/ai/startups/chat")
                .param("message", input);

        if (tc.jwt != null) {
            req = req.with(tc.jwt);
        }

        mockMvc.perform(req)
                .andExpect(status().is(tc.expectedStatus));

        if (tc.expectedStatus == 200) {
            mockMvc.perform(req)
                    .andExpect(content().string(reply));
        }
    }

    record TestCase(String role, JwtRequestPostProcessor jwt, int expectedStatus) {
    }
}
