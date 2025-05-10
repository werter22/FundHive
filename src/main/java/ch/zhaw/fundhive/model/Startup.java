package ch.zhaw.fundhive.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("startups")
public class Startup {
    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private IndustryType industry;
    @NonNull
    private Double valuation;
    @NonNull
    private StartupFundingStatus fundingStatus;
    @NonNull
    private String aiRating = "0.0";
    @NonNull
    private String ownerId;
}
