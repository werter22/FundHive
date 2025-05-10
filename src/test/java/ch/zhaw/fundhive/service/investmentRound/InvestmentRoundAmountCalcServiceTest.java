package ch.zhaw.fundhive.service.investmentRound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class InvestmentRoundAmountCalcServiceTest {

    private InvestmentTransactionRepository repository;
    private InvestmentRoundAmountCalcService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(InvestmentTransactionRepository.class);
        service = new InvestmentRoundAmountCalcService();
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @ParameterizedTest
    @CsvSource({
            "1000.0,2000.0,3000.0",
            "500.0,1500.0,2000.0",
            "0.0,0.0,0.0",
            "123.45,876.55,1000.0"
    })
    void getTotalRaised_calculatesSumCorrectly(double amount1, double amount2, double expectedSum) {
        InvestmentTransaction t1 = new InvestmentTransaction();
        t1.setAmount(amount1);
        InvestmentTransaction t2 = new InvestmentTransaction();
        t2.setAmount(amount2);

        when(repository.findByInvestmentRoundId("R1")).thenReturn(List.of(t1, t2));

        double result = service.getTotalRaised("R1");
        assertEquals(expectedSum, result, 0.001);
    }
}
