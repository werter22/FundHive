package ch.zhaw.fundhive.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InvestorPortfolioDTO {
    private InvestmentSummaryDTO summary;
    private List<InvestmentAllTransactionsDTO> transactions;
}
