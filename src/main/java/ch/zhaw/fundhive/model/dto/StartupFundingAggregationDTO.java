package ch.zhaw.fundhive.model.dto;

import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StartupFundingAggregationDTO {
    private StartupFundingStatus fundingStatus; // Grouped by funding status
    private int count; // Number of startups in that funding stage
    private List<String> startupIds; // List of startup IDs in this category
}
