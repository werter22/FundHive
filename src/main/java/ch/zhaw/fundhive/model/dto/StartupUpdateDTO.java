package ch.zhaw.fundhive.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;

@Getter
@Setter
@NoArgsConstructor
public class StartupUpdateDTO {
    private String name;
    private String description;
    private IndustryType industry;
    private Double valuation;
    private StartupFundingStatus fundingStatus;
}