package ch.zhaw.fundhive.repository.custom;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomInvestmentTransactionRepositoryImpl implements CustomInvestmentTransactionRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<InvestmentTransaction> filterInvestmentTransactions(String investmentRoundId, String investorId,
            Double minAmount, Double maxAmount, LocalDate startDate, LocalDate endDate) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (investmentRoundId != null) {
            criteriaList.add(Criteria.where("investmentRoundId").is(investmentRoundId));
        }

        if (investorId != null) {
            criteriaList.add(Criteria.where("investorId").is(investorId));
        }

        if (minAmount != null) {
            criteriaList.add(Criteria.where("amount").gte(minAmount));
        }

        if (maxAmount != null) {
            criteriaList.add(Criteria.where("amount").lte(maxAmount));
        }

        if (startDate != null) {
            criteriaList.add(Criteria.where("date").gte(startDate));
        }

        if (endDate != null) {
            criteriaList.add(Criteria.where("date").lte(endDate));
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return mongoTemplate.find(query, InvestmentTransaction.class);
    }

    @Override
    public FundingOverviewDTO getFundingOverview(String startupId) {
        MatchOperation matchStartup = match(Criteria.where("startupId").is(startupId));

        GroupOperation groupStats = group("startupId")
                .count().as("roundCount")
                .sum("amount").as("totalRaised")
                .avg("amount").as("avgPerRound");

        Aggregation aggregation = newAggregation(
                matchStartup,
                groupStats);

        AggregationResults<FundingOverviewDTO> results = mongoTemplate.aggregate(
                aggregation,
                InvestmentTransaction.class,
                FundingOverviewDTO.class);

        return results.getUniqueMappedResult();
    }
}
