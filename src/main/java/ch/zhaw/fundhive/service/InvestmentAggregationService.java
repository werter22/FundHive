package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestmentAggregationService {

    private final InvestmentTransactionRepository transactionRepository;
    private final InvestmentRoundRepository roundRepository;

    public void recalculateAmountRaised(String investmentRoundId) {
        // Sum all transactions related to the round
        List<InvestmentTransaction> transactions = transactionRepository.findByInvestmentRoundId(investmentRoundId);
        double total = transactions.stream()
                .mapToDouble(InvestmentTransaction::getAmount)
                .sum();

        roundRepository.findById(investmentRoundId).ifPresent(round -> {
            round.setAmount_raised(total);

            // Optionally close the round
            if (round.getStatus() == InvestmentStatus.OPEN && total >= round.getGoal_amount()) {
                round.setStatus(InvestmentStatus.CLOSED);
            }

            roundRepository.save(round);
        });
    }
}
