package ch.zhaw.fundhive.service.investmentRound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;

@Service
public class InvestmentRoundAmountCalcService {

    @Autowired
    private InvestmentTransactionRepository repository;

    public double getTotalRaised(String roundId) {
        return repository.findByInvestmentRoundId(roundId)
                .stream()
                .mapToDouble(InvestmentTransaction::getAmount)
                .sum();
    }

}
