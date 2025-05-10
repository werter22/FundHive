package ch.zhaw.fundhive.service.investmentRound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.dto.InvestmentRoundCreateDTO;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;

@ExtendWith(MockitoExtension.class)
public class InvestmentRoundServiceTest {

    @Mock
    private InvestmentRoundRepository repository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private InvestmentRoundService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_createsAndReturnsSavedRound_bluntly() throws Exception {
        InvestmentRoundCreateDTO dto = new InvestmentRoundCreateDTO();

        Field nameField = dto.getClass().getDeclaredField("round_name");
        nameField.setAccessible(true);
        nameField.set(dto, "Seed Round");

        Field goalField = dto.getClass().getDeclaredField("goal_amount");
        goalField.setAccessible(true);
        goalField.set(dto, 10000.0);

        Field dateField = dto.getClass().getDeclaredField("date");
        dateField.setAccessible(true);
        dateField.set(dto, "2025-01-01");

        Field startupField = dto.getClass().getDeclaredField("startupId");
        startupField.setAccessible(true);
        startupField.set(dto, "SU1");

        InvestmentRound saved = new InvestmentRound();
        when(repository.save(any())).thenReturn(saved);

        InvestmentRound result = service.create(dto);

        assertEquals(saved, result);
        verify(repository).save(any());
    }

    @Test
    void getRoundsByStartupId_returnsRounds() {
        List<InvestmentRound> mockRounds = List.of(new InvestmentRound());
        when(repository.findByStartupId("SU1")).thenReturn(mockRounds);

        List<InvestmentRound> result = service.getRoundsByStartupId("SU1");

        assertEquals(1, result.size());
    }

    @ParameterizedTest
    @CsvSource({
            // Complete range with status
            "1000.0, 5000.0, 2025-01-01, 2025-03-01, CLOSED",

            // Only max and endDate with status
            "null, 3000.0, null, 2025-04-01, OPEN",

            // Only min and startDate with status
            "500.0, null, 2025-01-01, null, UPCOMING",

            // Only status filter
            "null, null, null, null, OPEN",

            // Min and max without dates
            "250.0, 7500.0, null, null, null",

            // Only start date
            "null, null, 2024-12-31, null, CANCELLED",

            // Only end date
            "null, null, null, 2025-06-01, EXPIRED",

            // All filters null
            "null, null, null, null, null"
    })
    void getAllInvestmentRounds_appliesFiltersCorrectly(
            String minStr, String maxStr, String startStr, String endStr, String statusStr) {

        Double min = "null".equals(minStr) ? null : Double.valueOf(minStr);
        Double max = "null".equals(maxStr) ? null : Double.valueOf(maxStr);
        LocalDate start = "null".equals(startStr) ? null : LocalDate.parse(startStr);
        LocalDate end = "null".equals(endStr) ? null : LocalDate.parse(endStr);
        InvestmentStatus status = "null".equals(statusStr) ? null : InvestmentStatus.valueOf(statusStr);

        when(mongoTemplate.find(any(Query.class), eq(InvestmentRound.class)))
                .thenReturn(List.of(new InvestmentRound()));

        List<InvestmentRound> result = service.getAllInvestmentRounds(min, max, start, end, status);

        assertFalse(result.isEmpty());
        verify(mongoTemplate).find(any(Query.class), eq(InvestmentRound.class));
    }

}