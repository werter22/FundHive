package ch.zhaw.fundhive.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvestmentTransactionCreateDTO {
    private String investmentRoundId;
    private Double amount;

}
