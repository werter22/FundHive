package ch.zhaw.fundhive.service.investor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.dto.InvestmentAllTransactionsDTO;
import ch.zhaw.fundhive.model.dto.InvestorPortfolioDTO;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import ch.zhaw.fundhive.service.helpers.ResolveHelperService;

@Service
public class InvestorPortfolioViewService {

    @Autowired
    private InvestmentTransactionRepository investmentTransactionRepository;

    @Autowired
    private ResolveHelperService resolveHelperService;

    /* --- Investor Portfolio View --- */

    public InvestorPortfolioDTO getInvestorPortfolio(String investorId) {

        List<InvestmentTransaction> transactions = investmentTransactionRepository.findByInvestorId(investorId);

        double totalAmount = transactions.stream()
                .mapToDouble(InvestmentTransaction::getAmount)
                .sum();

        List<InvestmentAllTransactionsDTO> dtoList = transactions.stream()
                .map(tx -> {
                    String roundId = tx.getInvestmentRoundId();
                    String roundName = resolveHelperService.resolveRoundName(roundId);
                    String startupId = resolveHelperService.resolveStartupId(roundId);
                    String startupName = resolveHelperService.resolveStartupName(startupId);
                    String investorName = resolveHelperService.resolveInvestorName(investorId);

                    return new InvestmentAllTransactionsDTO(
                            tx.getId(),
                            investorId,
                            investorName,
                            roundId,
                            roundName,
                            startupId,
                            startupName,
                            tx.getAmount(),
                            tx.getDate());
                })
                .toList();

        return new InvestorPortfolioDTO(totalAmount, transactions.size(), dtoList);
    }

}
