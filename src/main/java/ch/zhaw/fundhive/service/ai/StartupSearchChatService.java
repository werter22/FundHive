package ch.zhaw.fundhive.service.ai;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ai.chat.client.ChatClient;
import ch.zhaw.fundhive.tools.StartupTools;

@Service
public class StartupSearchChatService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private StartupTools startupTools;

    public String handleStartupSearchChat(String message) {
        String prompt = """
                You are a startup scout helping investors discover promising startups based on their interest or focus.

                Your job is to read the investor's message and recommend startups that best align with it.

                Guidelines:
                - Sound conversational, helpful, and professional — not robotic.
                - Recommend only startups that clearly match the user's query.
                - If no good matches exist, politely say so.
                - Don't list all startups — be selective and relevant.
                - Use natural language. Example: "If you're looking for sustainable packaging, GreenLeaf might interest you."
                - Mention the startup's name, sector, and a short reason why it's relevant.

                Startups you can choose from:
                %s

                Investor's query: %s

                Respond with a helpful message:
                """;

        return chatClient
                .prompt(prompt)
                .tools(startupTools)
                .user(message)
                .call()
                .content();
    }
}
