package ch.zhaw.fundhive.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Document(collection = "investmentTransactions")
public class InvestmentTransaction {

    @Id
    private String id;

    @NonNull
    private String investmentRoundId;

    @NonNull
    private String investorId;

    @NonNull
    private Double amount;

    @NonNull
    private String date;
}
