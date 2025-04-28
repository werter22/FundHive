package ch.zhaw.fundhive.repository;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRoundRepository extends MongoRepository<InvestmentRound, String> {

        List<InvestmentRound> findByStartupId(String startupId);

        @Aggregation({
                        "{ '$addFields': { 'amountRaisedNumeric': { '$toDouble': '$amountRaised' } } }",
                        "{ '$match': { 'amountRaisedNumeric': { '$gte': ?0, '$lte': ?1 }, 'date': { '$gte': ?2, '$lte': ?3 } } }",
                        "{ '$project': { 'amountRaisedNumeric': 0 } }"
        })
        List<InvestmentRound> findByAmountRaisedAndDateBetween(
                        double minAmountRaised,
                        double maxAmountRaised,
                        LocalDate startDate,
                        LocalDate endDate);

        @Aggregation({
                        "{ '$match': { 'startupId': ?0 } }",
                        "{ '$group': { '_id': '$startupId', 'totalRaised': { '$sum': '$amount_raised' }, 'roundCount': { '$sum': 1 } } }",
                        "{ '$project': { 'startupId': '$_id', 'totalRaised': 1, 'roundCount': 1, '_id': 0 } }"
        })
        FundingOverviewDTO getFundingOverview(String startupId);

}
