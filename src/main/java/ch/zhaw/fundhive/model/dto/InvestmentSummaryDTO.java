package ch.zhaw.fundhive.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvestmentSummaryDTO {
    private double totalAmount;
    private int transactionCount;
}
