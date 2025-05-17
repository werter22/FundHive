package ch.zhaw.fundhive.service.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.repository.StartupRepository;

@Service
public class StartupDescriptionChatService {

        @Autowired
        private StartupRepository startupRepository;

        @Autowired
        private ChatClient chatClient;

        public String improveStartupDescriptionChat(
                        String startupId,
                        String htmlContent,
                        String userInput) {

                Startup startup = startupRepository.findById(startupId)
                                .orElseThrow(() -> new RuntimeException("Startup not found"));

                String prompt = """
                                    You are a startup pitch assistant helping founders improve their description text.

                                    Rules:
                                    - Do NOT invent new information.
                                    - Base your suggestions only on the provided HTML and startup data.
                                    - Focus on better wording, structure, grammar, and investor appeal.
                                    - Keep the tone professional and persuasive.
                                    - Use semantic HTML only (like <p>, <strong>, <ul>, <li>, etc.).
                                    - DO NOT include any inline CSS styles (no `style="..."` attributes).
                                    - DO NOT wrap your output in markdown syntax like ```html or ```.

                                    Return format:
                                    Only return the corrected, clean HTML content without any additional formatting or explanations.

                                    Startup info:
                                    Name: %s
                                    Industry: %s
                                    Funding: %s
                                    Valuation: %.2f

                                    Current description:
                                    %s

                                    User request: %s
                                """
                                .formatted(
                                                startup.getName(),
                                                startup.getIndustry().name(),
                                                startup.getFundingStatus().name(),
                                                startup.getValuation(),
                                                htmlContent,
                                                userInput);

                return chatClient.prompt(prompt)
                                .user(userInput)
                                .call()
                                .content();
        }
}
