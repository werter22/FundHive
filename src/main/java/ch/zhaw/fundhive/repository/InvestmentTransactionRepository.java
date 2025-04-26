package ch.zhaw.fundhive.repository;

import ch.zhaw.fundhive.model.InvestmentTransaction;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentTransactionRepository
                extends MongoRepository<InvestmentTransaction, String> {
        List<InvestmentTransaction> findByInvestorId(String investorId);

        List<InvestmentTransaction> findByInvestmentRoundId(String investmentRoundId);
}
