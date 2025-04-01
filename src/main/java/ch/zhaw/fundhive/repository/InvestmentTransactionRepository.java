package ch.zhaw.fundhive.repository;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentTransactionRepository extends MongoRepository<InvestmentTransaction, String> {
}
