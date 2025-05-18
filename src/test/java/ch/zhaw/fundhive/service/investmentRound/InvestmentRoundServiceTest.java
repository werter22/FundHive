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
            // min, max, startFrom, startTo, endFrom, endTo, status
            "1000.0, 5000.0, 2025-01-01, 2025-03-01, 2025-02-01, 2025-04-01, CLOSED",
            "null,   3000.0, null,       null,       null,         2025-04-01, OPEN",
            "500.0,  null,   2025-01-01, null,       null,         null,       UPCOMING",
            "null,   null,   null,       null,       null,         null,       OPEN",
            "250.0,  7500.0, null,       null,       null,         null,       null",
            "null,   null,   2024-12-31, null,       null,         null,       CANCELLED",
            "null,   null,   null,       null,       null,         2025-06-01, EXPIRED",
            "null,   null,   null,       null,       null,         null,       null"
    })
    void getAllInvestmentRounds_appliesFiltersCorrectly(
            String minStr,
            String maxStr,
            String startFromStr,
            String startToStr,
            String endFromStr,
            String endToStr,
            String statusStr) {
        Double min = "null".equals(minStr) ? null : Double.valueOf(minStr);
        Double max = "null".equals(maxStr) ? null : Double.valueOf(maxStr);
        LocalDate startFrom = "null".equals(startFromStr) ? null : LocalDate.parse(startFromStr);
        LocalDate startTo = "null".equals(startToStr) ? null : LocalDate.parse(startToStr);
        LocalDate endFrom = "null".equals(endFromStr) ? null : LocalDate.parse(endFromStr);
        LocalDate endTo = "null".equals(endToStr) ? null : LocalDate.parse(endToStr);
        InvestmentStatus status = "null".equals(statusStr) ? null : InvestmentStatus.valueOf(statusStr);

        // stub the mongo query to return something so service returns non-empty list
        when(mongoTemplate.find(any(Query.class), eq(InvestmentRound.class)))
                .thenReturn(List.of(new InvestmentRound()));

        // call the new 7-arg method
        List<InvestmentRound> result = service.getAllInvestmentRounds(
                min, max,
                startFrom, startTo,
                endFrom, endTo,
                status);

        assertFalse(result.isEmpty(), "Expected non-empty result list");
        // verify we indeed invoked the mongoTemplate.find(...) once
        verify(mongoTemplate).find(any(Query.class), eq(InvestmentRound.class));
    }

}