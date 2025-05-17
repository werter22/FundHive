package ch.zhaw.fundhive.service.ai;

import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.repository.InvestorRepository;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InvestorMockAiRatingService {

        @Autowired
        InvestorRepository investorRepo;
        @Autowired
        InvestmentTransactionRepository txRepo;
        @Autowired
        InvestmentRoundRepository roundRepo;

        private static final double BASELINE = 3.0;
        private static final double SCALE = 2.0; // maps normalized score [0,1] → [3.0,5.0]
        private static final int MAX_TX_COUNT = 50;
        private static final double MAX_VOLUME = 1_000_000.0;
        private static final double RECENCY_WINDOW = 90.0; // days

        /**
         * Runs every 14 days at 3AM, recomputing each investor's mock rating
         * based on activity, volume, success rate, and recency.
         */
        @Scheduled(cron = "0 0 3 */14 * *")
        public void rateAllInvestors() {
                List<Investor> all = investorRepo.findAll();
                LocalDate today = LocalDate.now();

                for (Investor inv : all) {
                        List<InvestmentTransaction> txs = txRepo.findByInvestorId(inv.getId());

                        double count = txs.size();
                        double totalVolume = txs.stream().mapToDouble(InvestmentTransaction::getAmount).sum();
                        long success = txs.stream()
                                        .filter(tx -> {
                                                InvestmentRound round = roundRepo.findById(tx.getInvestmentRoundId())
                                                                .orElseThrow();
                                                return round.getStatus() == InvestmentStatus.CLOSED;
                                        })
                                        .count();

                        LocalDate lastDate;
                        if (!txs.isEmpty()) {
                                lastDate = txs.stream()
                                                .map(tx -> LocalDate.parse(tx.getDate(), DateTimeFormatter.ISO_DATE))
                                                .max(LocalDate::compareTo)
                                                .orElse(today.minusDays((long) RECENCY_WINDOW));
                        } else {
                                lastDate = today.minusDays((long) RECENCY_WINDOW);
                        }

                        double daysSince = (double) java.time.temporal.ChronoUnit.DAYS.between(lastDate, today);

                        // normalize each metric to [0,1]
                        double normA = Math.min(count / MAX_TX_COUNT, 1.0);
                        double normV = Math.min(totalVolume / MAX_VOLUME, 1.0);
                        double normS = count > 0 ? ((double) success / count) : 0.0;
                        double normR = Math.max(0.0, 1.0 - daysSince / RECENCY_WINDOW);

                        // weighted engagement score
                        double engagement = 0.3 * normA
                                        + 0.3 * normV
                                        + 0.3 * normS
                                        + 0.1 * normR;

                        // map to [3.0,5.0]
                        double rawScore = BASELINE + engagement * SCALE;
                        double clamped = Math.max(BASELINE, Math.min(BASELINE + SCALE, rawScore));

                        String rating = BigDecimal.valueOf(clamped)
                                        .setScale(1, RoundingMode.HALF_UP)
                                        .toString();

                        inv.setAiRating(rating);
                }

                investorRepo.saveAll(all);
        }
}
