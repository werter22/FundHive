package ch.zhaw.fundhive.service.investmentRound;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.dto.InvestmentRoundCreateDTO;
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

    public InvestmentRound create(InvestmentRoundCreateDTO dto) {
        InvestmentRound round = new InvestmentRound();

        round.setRound_name(dto.getRound_name());
        round.setGoal_amount(dto.getGoal_amount());
        round.setDate(dto.getDate());
        round.setStartupId(dto.getStartupId());

        LocalDate startDate = LocalDate.parse(dto.getDate());
        round.setEndDate(startDate.plusDays(90).toString());

        return repository.save(round);
    }

    /* --- Gets all the rounds of any given startup --- */

    public List<InvestmentRound> getRoundsByStartupId(String startupId) {
        return repository.findByStartupId(startupId);
    }

    /* --- Filter method for Admin audit --- */

    public List<InvestmentRound> getAllInvestmentRounds(
            Double minAmountRaised,
            Double maxAmountRaised,
            LocalDate startDateFrom,
            LocalDate startDateTo,
            LocalDate endDateFrom,
            LocalDate endDateTo,
            InvestmentStatus status) {

        Query query = new Query();

        // 1) amount_raised range
        if (minAmountRaised != null || maxAmountRaised != null) {
            Criteria crit = Criteria.where("amount_raised");
            if (minAmountRaised != null)
                crit = crit.gte(minAmountRaised);
            if (maxAmountRaised != null)
                crit = crit.lte(maxAmountRaised);
            query.addCriteria(crit);
        }

        // 2) start‐date range
        if (startDateFrom != null || startDateTo != null) {
            Criteria crit = Criteria.where("date");
            if (startDateFrom != null)
                crit = crit.gte(startDateFrom.toString());
            if (startDateTo != null)
                crit = crit.lte(startDateTo.toString());
            query.addCriteria(crit);
        }

        // 3) end‐date range
        if (endDateFrom != null || endDateTo != null) {
            Criteria crit = Criteria.where("endDate");
            if (endDateFrom != null)
                crit = crit.gte(endDateFrom.toString());
            if (endDateTo != null)
                crit = crit.lte(endDateTo.toString());
            query.addCriteria(crit);
        }

        // 4) status
        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        return mongoTemplate.find(query, InvestmentRound.class);
    }
}
