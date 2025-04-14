package ch.zhaw.fundhive.repository.custom;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;

import java.time.LocalDate;
import java.util.List;

public interface CustomInvestmentTransactionRepository {
    List<InvestmentTransaction> filterInvestmentTransactions(
            String investmentRoundId,
            String investorId,
            Double minAmount,
            Double maxAmount,
            LocalDate startDate,
            LocalDate endDate);

    FundingOverviewDTO getFundingOverview(String startupId);
}
