package ch.zhaw.fundhive.repository;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

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
}
