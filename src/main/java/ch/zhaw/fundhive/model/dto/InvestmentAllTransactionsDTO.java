package ch.zhaw.fundhive.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvestmentAllTransactionsDTO {
    private String transactionId;
    private String investorId;
    private String investorName;
    private String roundId;
    private String roundName;
    private String startupId;
    private String startupName;
    private double amount;
    private String date;
}
