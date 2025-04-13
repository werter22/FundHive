package ch.zhaw.fundhive.repository;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.dto.StartupFundingAggregationDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.repository.custom.CustomStartupRepository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StartupRepository extends MongoRepository<Startup, String>, CustomStartupRepository {

        // Find all startups of a specific industry
        List<Startup> findByIndustry(IndustryType industry);

        // Find all startups within a specific funding stage
        List<Startup> findByFundingStatus(StartupFundingStatus fundingStatus);

        // Find all startups with a valuation greater than a certain amount
        List<Startup> findByValuationGreaterThan(double valuation);

        // Find all startups within a valuation range
        @Aggregation({
                        "{ '$addFields': { 'valuationNumeric': { '$toDouble': '$valuation' } } }",
                        "{ '$match': { 'valuationNumeric': { '$gte': ?0, '$lte': ?1 } } }",
                        "{ '$project': { 'valuationNumeric': 0 } }"
        })
        List<Startup> findByValuationBetween(double minValuation, double maxValuation);

        // Find all startups in a specific industry with a funding stage
        List<Startup> findByIndustryAndFundingStatus(IndustryType industry, StartupFundingStatus fundingStatus);

        // Find all startups in an industry within a valuation range
        List<Startup> findByIndustryAndValuationBetween(IndustryType industry, double minValuation,
                        double maxValuation);

        // Aggregation query: Group startups by funding status and count them
        @Aggregation({
                        "{ '$group': { '_id': '$fundingStatus', 'count': { '$sum': 1 }, 'startupIds': { '$push': '$_id' } } }",
                        "{ '$project': { 'fundingStatus': '$_id', 'count': 1, 'startupIds': 1, '_id': 0 } }"
        })
        List<StartupFundingAggregationDTO> getFundingStatusAggregation();
}
