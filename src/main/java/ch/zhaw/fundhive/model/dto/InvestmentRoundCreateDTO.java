package ch.zhaw.fundhive.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvestmentRoundCreateDTO {

    private String round_name;
    private Double goal_amount;
    private String date;
    private String startupId;
}