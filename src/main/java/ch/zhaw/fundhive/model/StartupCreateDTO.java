package ch.zhaw.fundhive.model;

import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StartupCreateDTO {
    private String name;
    private String description;
    private IndustryType industry;
    private double valuation;
    private StartupFundingStatus fundingStatus;
    private double aiRating;
}
