package ch.zhaw.fundhive.service.investmentRound;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;

@Service
public class InvestmentRoundStatusService {

    @Autowired
    private InvestmentRoundRepository repository;

    /* --- Round status management --- */

    // manually cancel round
    public Optional<InvestmentRound> cancelRound(String roundId) {
        Optional<InvestmentRound> roundOptional = repository.findById(roundId);

        if (roundOptional.isPresent()) {
            InvestmentRound round = roundOptional.get();

            if (round.getStatus() == InvestmentStatus.UPCOMING || round.getStatus() == InvestmentStatus.OPEN) {
                round.setStatus(InvestmentStatus.CANCELLED);
                repository.save(round);
                return Optional.of(round);
            }
        }
        return Optional.empty();
    }

    // manually open round before current start date
    public Optional<InvestmentRound> openRound(String roundId) {
        Optional<InvestmentRound> roundOptional = repository.findById(roundId);

        if (roundOptional.isPresent()) {
            InvestmentRound round = roundOptional.get();

            if (round.getStatus() == InvestmentStatus.UPCOMING) {
                LocalDate today = LocalDate.now();
                LocalDate startDate = LocalDate.parse(round.getDate());

                // Entrepreneur opens early → reset start + end
                if (today.isBefore(startDate)) {
                    round.setDate(today.toString());
                    round.setEndDate(today.plusDays(90).toString());
                }

                round.setStatus(InvestmentStatus.OPEN);
                repository.save(round);
                return Optional.of(round);
            }
        }

        return Optional.empty();
    }

    // closes round if target amount is reached or surpassed
    public void tryCloseIfGoalReached(InvestmentRound round) {
        if (round.getStatus() == InvestmentStatus.OPEN &&
                round.getAmount_raised() >= round.getGoal_amount()) {

            round.setStatus(InvestmentStatus.CLOSED);
            repository.save(round);
        }
    }

    // Scheduled method: runs every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void autoExpireRounds() {
        expireOutdatedRounds();
        autoOpenScheduledRounds();
    }

    // Round expires after 90 days
    public void expireOutdatedRounds() {
        List<InvestmentRound> rounds = repository.findAll();
        LocalDate today = LocalDate.now();

        for (InvestmentRound round : rounds) {
            InvestmentStatus status = round.getStatus();
            boolean isInactive = status == InvestmentStatus.CLOSED || status == InvestmentStatus.CANCELLED
                    || status == InvestmentStatus.EXPIRED;

            if (!isInactive) {
                LocalDate endDate = LocalDate.parse(round.getEndDate());
                if (today.isAfter(endDate)) {
                    round.setStatus(InvestmentStatus.EXPIRED);
                    repository.save(round);
                }
            }
        }
    }

    // Open rounds automatically on start date
    public void autoOpenScheduledRounds() {
        List<InvestmentRound> rounds = repository.findAll();
        LocalDate today = LocalDate.now();

        for (InvestmentRound round : rounds) {
            if (round.getStatus() == InvestmentStatus.UPCOMING) {
                LocalDate startDate = LocalDate.parse(round.getDate());
                if (today.equals(startDate)) {
                    round.setStatus(InvestmentStatus.OPEN);
                    repository.save(round);
                }
            }
        }
    }

}
