package ch.zhaw.fundhive.repository;

import ch.zhaw.fundhive.model.InvestmentRound;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRoundRepository extends MongoRepository<InvestmentRound, String> {

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
}
