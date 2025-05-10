package ch.zhaw.fundhive.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import ch.zhaw.fundhive.model.dto.InvestmentTransactionCreateDTO;
import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.investmentRound.InvestmentRoundAmountCalcService;
import ch.zhaw.fundhive.service.investmentRound.InvestmentRoundStatusService;

public class InvestmentTransactionServiceTest {

    @Mock
    private InvestmentTransactionRepository transactionRepo;

    @Mock
    private InvestmentRoundRepository roundRepo;

    @Mock
    private InvestmentRoundAmountCalcService calcService;

    @Mock
    private InvestmentRoundStatusService statusService;

    @Mock
    private UserService userService;

    @InjectMocks
    private InvestmentTransactionService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_returnsEmptyIfRoundNotFound() throws Exception {
        InvestmentTransactionCreateDTO dto = new InvestmentTransactionCreateDTO();

        Field roundIdField = InvestmentTransactionCreateDTO.class.getDeclaredField("investmentRoundId");
        roundIdField.setAccessible(true);
        roundIdField.set(dto, "R1");

        Field amountField = InvestmentTransactionCreateDTO.class.getDeclaredField("amount");
        amountField.setAccessible(true);
        amountField.set(dto, 1000.0);

        when(roundRepo.findById("R1")).thenReturn(Optional.empty());

        assertTrue(service.create(dto).isEmpty());
    }

    @Test
    void create_returnsEmptyIfRoundNotOpen() throws Exception {
        InvestmentRound round = new InvestmentRound();
        round.setStatus(InvestmentStatus.CLOSED);

        InvestmentTransactionCreateDTO dto = new InvestmentTransactionCreateDTO();

        Field roundIdField = InvestmentTransactionCreateDTO.class.getDeclaredField("investmentRoundId");
        roundIdField.setAccessible(true);
        roundIdField.set(dto, "R1");

        when(roundRepo.findById("R1")).thenReturn(Optional.of(round));

        assertTrue(service.create(dto).isEmpty());
    }

    @Test
    void create_savesTransactionAndUpdatesRound() throws Exception {
        // Arrange
        InvestmentTransactionCreateDTO dto = new InvestmentTransactionCreateDTO();

        Field roundIdField = InvestmentTransactionCreateDTO.class.getDeclaredField("investmentRoundId");
        roundIdField.setAccessible(true);
        roundIdField.set(dto, "R1");

        Field amountField = InvestmentTransactionCreateDTO.class.getDeclaredField("amount");
        amountField.setAccessible(true);
        amountField.set(dto, 1000.0);

        InvestmentRound round = new InvestmentRound();
        round.setId("R1");
        round.setStatus(InvestmentStatus.OPEN);

        InvestmentTransaction savedTx = new InvestmentTransaction();
        savedTx.setAmount(1000.0);

        when(roundRepo.findById("R1")).thenReturn(Optional.of(round));
        when(userService.getCurrentUserId()).thenReturn("INV1");
        when(transactionRepo.save(any())).thenReturn(savedTx);
        when(calcService.getTotalRaised("R1")).thenReturn(2500.0);

        // Act
        Optional<InvestmentTransaction> result = service.create(dto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1000.0, result.get().getAmount());

        verify(transactionRepo).save(any());
        verify(roundRepo).save(round);
        verify(statusService).tryCloseIfGoalReached(round);
    }

}
