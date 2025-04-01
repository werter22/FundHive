package ch.zhaw.fundhive.repository;

import ch.zhaw.fundhive.model.InvestmentRound;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRoundRepository extends MongoRepository<InvestmentRound, String> {
}
