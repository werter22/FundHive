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
