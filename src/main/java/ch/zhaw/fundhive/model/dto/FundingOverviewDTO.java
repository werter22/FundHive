package ch.zhaw.fundhive.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FundingOverviewDTO {
    private String startupId;
    private double totalRaised;
    private long roundCount;
}
