package ch.zhaw.fundhive.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import ch.zhaw.fundhive.model.enums.InvestmentStatus;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "investmentRounds")
public class InvestmentRound {

    @Id
    private String id;

    @NonNull
    private String round_name;

    @NonNull
    private Double amount_raised;

    @NonNull
    private Double goal_amount;

    @NonNull
    private String date;

    @NonNull
    private String startupId;

    @NonNull
    private InvestmentStatus status = InvestmentStatus.UPCOMING;
}