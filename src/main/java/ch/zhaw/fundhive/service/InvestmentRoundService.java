package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvestmentRoundService {

    @Autowired
    private InvestmentRoundRepository repository;

    // --- CRUD methods ---

    public InvestmentRound create(InvestmentRound round) {
        LocalDate startDate = LocalDate.parse(round.getDate());
        round.setEndDate(startDate.plusDays(90).toString());
        return repository.save(round);
    }

    public List<InvestmentRound> getAll() {
        return repository.findAll();
    }

    public InvestmentRound update(String id, InvestmentRound updated) {
        return repository.findById(id).map(existing -> {
            existing.setRound_name(updated.getRound_name());
            existing.setAmount_raised(updated.getAmount_raised());
            existing.setGoal_amount(updated.getGoal_amount());
            existing.setDate(updated.getDate());
            existing.setStartupId(updated.getStartupId());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Round not found"));
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    // --- Round status management ---

    // closes round when target amount is reached
    public void checkAndClose(String id) {
        repository.findById(id).ifPresent(round -> {
            double raised = round.getAmount_raised();
            double goal = round.getGoal_amount();

            if (raised >= goal && round.getStatus() == InvestmentStatus.OPEN) {
                round.setStatus(InvestmentStatus.CLOSED);
                repository.save(round);
            }
        });
    }

    // manually cancel round
    public void cancelRound(String id) {
        repository.findById(id).ifPresent(round -> {
            round.setStatus(InvestmentStatus.CANCELLED);
            repository.save(round);
        });
    }

    // manually open round
    public void openRound(String id) {
        repository.findById(id).ifPresent(round -> {
            if (round.getStatus() == InvestmentStatus.UPCOMING) {
                round.setStatus(InvestmentStatus.OPEN);
                repository.save(round);
            } else {
                throw new IllegalStateException("Only UPCOMING rounds can be opened.");
            }
        });
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

    // --- Filter methods ---

    public List<InvestmentRound> getFilteredInvestmentRounds(double minAmountRaised, double maxAmountRaised,
            LocalDate startDate, LocalDate endDate) {
        return repository.findByAmountRaisedAndDateBetween(
                minAmountRaised, maxAmountRaised, startDate, endDate);
    }

}
