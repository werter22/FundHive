package ch.zhaw.fundhive.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvestmentAllTransactionsDTO {
    private String transactionId;
    private String investorId;
    private String investmentRoundId;
    private String startupId;
    private double amount;
    private String date; // You can use LocalDate if you prefer
}
