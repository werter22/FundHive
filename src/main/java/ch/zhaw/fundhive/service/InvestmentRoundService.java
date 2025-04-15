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

    public List<InvestmentRound> getAll() {
        return repository.findAll();
    }

    public InvestmentRound create(InvestmentRound round) {
        round.setStatus(InvestmentStatus.OPEN);
        return repository.save(round);
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

    public List<InvestmentRound> getFilteredInvestmentRounds(double minAmountRaised, double maxAmountRaised,
            LocalDate startDate, LocalDate endDate) {
        return repository.findByAmountRaisedAndDateBetween(
                minAmountRaised, maxAmountRaised, startDate, endDate);
    }

}
