package ch.zhaw.fundhive.service.ai;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.repository.StartupRepository;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class StartupAiRatingService {

    @Autowired
    private StartupRepository startupRepo;

    @Autowired
    private OpenAiChatModel chatModel;

    /**
     * Runs automatically every 14 days at 2 AM server time,
     * recomputing and persisting each startup’s AI rating.
     */
    @Scheduled(cron = "0 0 2 */14 * *")
    public void rateAllStartups() {
        List<Startup> all = startupRepo.findAll();
        for (Startup s : all) {
            BigDecimal rating = computeRating(s)
                    .max(BigDecimal.ZERO)
                    .min(BigDecimal.valueOf(5.0))
                    .setScale(1, RoundingMode.HALF_UP);

            s.setAiRating(rating.toString());
        }
        startupRepo.saveAll(all);
    }

    public String rateStartupOnCreation(Startup s) {
        BigDecimal raw = computeRating(s)
                .max(BigDecimal.ZERO)
                .min(BigDecimal.valueOf(5.0))
                .setScale(1, RoundingMode.HALF_UP);

        String rating = raw.toString();
        return rating;
    }

    /**
     * Builds a prompt from the startup’s fields, calls the LLM,
     * and parses its numeric output.
     */
    private BigDecimal computeRating(Startup s) {
        String desc = s.getDescription() != null ? s.getDescription() : "";

        String prompt = """
                You are a seasoned venture capital analyst.
                Given this startup:
                 • Name: %s
                 • Industry: %s
                 • Valuation: %s
                 • Description: %s

                On a scale from 0.0 (no potential) to 5.0 (outstanding),
                how likely is this startup to succeed in its industry?
                Answer *only* with a single decimal number.
                """
                .formatted(
                        s.getName(),
                        s.getIndustry(),
                        s.getValuation(),
                        desc);

        String raw = chatModel.call(prompt).trim();

        try {
            return new BigDecimal(raw);
        } catch (NumberFormatException ex) {
            // Fallback rating if LLM response isn't parseable
            return BigDecimal.ZERO;
        }
    }
}