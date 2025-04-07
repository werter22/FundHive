package ch.zhaw.fundhive.repository.custom;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

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
}
