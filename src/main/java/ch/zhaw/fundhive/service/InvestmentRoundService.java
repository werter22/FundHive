package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvestmentRoundService {

    @Autowired
    private InvestmentRoundRepository repository;

    @Autowired
    private org.springframework.data.mongodb.core.MongoTemplate mongoTemplate;

    /* --- CRUD methods --- */

    public InvestmentRound create(InvestmentRound round) {
        LocalDate startDate = LocalDate.parse(round.getDate());
        round.setEndDate(startDate.plusDays(90).toString());
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

    /* --- Round status management --- */

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

    /* --- Gets all the rounds of any given startup --- */

    public List<InvestmentRound> getRoundsByStartupId(String startupId) {
        return repository.findByStartupId(startupId);
    }

    /* --- Filter method for Admin audit --- */

    public List<InvestmentRound> getAllInvestmentRounds(
            Double minAmountRaised,
            Double maxAmountRaised,
            LocalDate startDate,
            LocalDate endDate,
            InvestmentStatus status) {

        Query query = new Query();

        if (minAmountRaised != null || maxAmountRaised != null) {
            Criteria amountCrit = Criteria.where("amount_raised");
            if (minAmountRaised != null) {
                amountCrit = amountCrit.gte(minAmountRaised);
            }
            if (maxAmountRaised != null) {
                amountCrit = amountCrit.lte(maxAmountRaised);
            }
            query.addCriteria(amountCrit);
        }

        if (startDate != null || endDate != null) {
            Criteria dateCrit = Criteria.where("date");
            if (startDate != null)
                dateCrit = dateCrit.gte(startDate.toString());
            if (endDate != null)
                dateCrit = dateCrit.lte(endDate.toString());
            query.addCriteria(dateCrit);
        }

        if (startDate != null || endDate != null) {
            Criteria endCrit = Criteria.where("endDate");
            if (startDate != null)
                endCrit = endCrit.gte(startDate.toString());
            if (endDate != null)
                endCrit = endCrit.lte(endDate.toString());
            query.addCriteria(endCrit);
        }

        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        return mongoTemplate.find(query, InvestmentRound.class);
    }

}
