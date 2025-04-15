package ch.zhaw.fundhive.repository.custom;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.util.List;

public class CustomStartupRepositoryImpl implements CustomStartupRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Startup> filterStartups(
            IndustryType industry,
            StartupFundingStatus fundingStatus,
            Double minValuation,
            Double maxValuation) {
        Query query = new Query();

        if (industry != null) {
            query.addCriteria(Criteria.where("industry").is(industry));
        }
        if (fundingStatus != null) {
            query.addCriteria(Criteria.where("fundingStatus").is(fundingStatus));
        }
        if (minValuation != null) {
            query.addCriteria(Criteria.where("valuation").gte(minValuation));
        }
        if (maxValuation != null) {
            query.addCriteria(Criteria.where("valuation").lte(maxValuation));
        }

        return mongoTemplate.find(query, Startup.class);
    }

    @Override
    public FundingOverviewDTO getFundingOverview(String startupId) {
        LookupOperation joinRounds = LookupOperation.newLookup()
                .from("investmentRounds")
                .localField("investmentRoundId")
                .foreignField("_id")
                .as("round");

        UnwindOperation unwindRound = unwind("round");

        MatchOperation matchStartup = match(Criteria.where("round.startupId").is(startupId));

        GroupOperation groupStats = group("round.startupId")
                .sum("amount").as("totalRaised")
                .count().as("roundCount");

        ProjectionOperation project = project()
                .and("_id").as("startupId")
                .andInclude("totalRaised", "roundCount");

        Aggregation aggregation = newAggregation(
                joinRounds,
                unwindRound,
                matchStartup,
                groupStats,
                project);

        AggregationResults<FundingOverviewDTO> results = mongoTemplate.aggregate(
                aggregation,
                InvestmentTransaction.class,
                FundingOverviewDTO.class);

        return results.getUniqueMappedResult();
    }
}
