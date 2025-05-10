package ch.zhaw.fundhive.service.investmentRound;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;

@ExtendWith(MockitoExtension.class)
class InvestmentRoundStatusServiceTest {

    @Mock
    private InvestmentRoundRepository repository;

    @InjectMocks
    private InvestmentRoundStatusService service;

    @Test
    void cancelRound_returnsEmptyIfRoundNotFound() {
        when(repository.findById("R1")).thenReturn(Optional.empty());

        Optional<InvestmentRound> result = service.cancelRound("R1");

        assertTrue(result.isEmpty());
        verify(repository).findById("R1");
        verify(repository, never()).save(any());
    }

    @ParameterizedTest
    @EnumSource(value = InvestmentStatus.class, names = { "UPCOMING", "OPEN" })
    void cancelRound_setsCancelledIfUpcomingOrOpen(InvestmentStatus initialStatus) {
        InvestmentRound round = new InvestmentRound();
        round.setId("R1");
        round.setStatus(initialStatus);

        when(repository.findById("R1")).thenReturn(Optional.of(round));

        Optional<InvestmentRound> result = service.cancelRound("R1");

        assertTrue(result.isPresent());
        assertEquals(InvestmentStatus.CANCELLED, result.get().getStatus());
        verify(repository).save(round);
    }

    @ParameterizedTest
    @EnumSource(value = InvestmentStatus.class, names = { "CANCELLED", "CLOSED", "EXPIRED" })
    void cancelRound_skipsIfStatusNotCancelable(InvestmentStatus initialStatus) {
        InvestmentRound round = new InvestmentRound();
        round.setId("R1");
        round.setStatus(initialStatus);

        when(repository.findById("R1")).thenReturn(Optional.of(round));

        Optional<InvestmentRound> result = service.cancelRound("R1");

        assertTrue(result.isEmpty());
        verify(repository, never()).save(any());
    }

    @Test
    void openRound_setsOpenIfStatusIsUpcoming() {
        InvestmentRound round = new InvestmentRound();
        round.setId("R1");
        round.setStatus(InvestmentStatus.UPCOMING);

        when(repository.findById("R1")).thenReturn(Optional.of(round));

        Optional<InvestmentRound> result = service.openRound("R1");

        assertTrue(result.isPresent());
        assertEquals(InvestmentStatus.OPEN, result.get().getStatus());
        verify(repository).save(round);
    }

    @ParameterizedTest
    @EnumSource(value = InvestmentStatus.class, names = { "OPEN", "CLOSED", "CANCELLED", "EXPIRED" })
    void openRound_doesNothingIfStatusIsNotUpcoming(InvestmentStatus status) {
        InvestmentRound round = new InvestmentRound();
        round.setId("R1");
        round.setStatus(status);

        when(repository.findById("R1")).thenReturn(Optional.of(round));

        Optional<InvestmentRound> result = service.openRound("R1");

        assertTrue(result.isEmpty());
        verify(repository, never()).save(any());
    }

    @Test
    void tryCloseIfGoalReached_closesRoundWhenGoalReachedAndStatusOpen() {
        InvestmentRound round = new InvestmentRound();
        round.setStatus(InvestmentStatus.OPEN);
        round.setAmount_raised(10000.0);
        round.setGoal_amount(10000.0);

        service.tryCloseIfGoalReached(round);

        assertEquals(InvestmentStatus.CLOSED, round.getStatus());
        verify(repository).save(round);
    }

    @Test
    void tryCloseIfGoalReached_doesNothingIfStatusNotOpen() {
        InvestmentRound round = new InvestmentRound();
        round.setStatus(InvestmentStatus.UPCOMING); // Not OPEN
        round.setAmount_raised(10000.0);
        round.setGoal_amount(5000.0);

        service.tryCloseIfGoalReached(round);

        assertEquals(InvestmentStatus.UPCOMING, round.getStatus());
        verify(repository, never()).save(any());
    }

    @Test
    void tryCloseIfGoalReached_doesNothingIfGoalNotReached() {
        InvestmentRound round = new InvestmentRound();
        round.setStatus(InvestmentStatus.OPEN);
        round.setAmount_raised(4000.0);
        round.setGoal_amount(5000.0); // Not enough raised

        service.tryCloseIfGoalReached(round);

        assertEquals(InvestmentStatus.OPEN, round.getStatus());
        verify(repository, never()).save(any());
    }

    @Test
    void expireOutdatedRounds_marksExpiredIfEndDatePassedAndNotClosedOrCancelled() {
        InvestmentRound activeRound = new InvestmentRound();
        activeRound.setStatus(InvestmentStatus.OPEN);
        activeRound.setEndDate(LocalDate.now().minusDays(1).toString());

        when(repository.findAll()).thenReturn(List.of(activeRound));

        service.expireOutdatedRounds();

        assertEquals(InvestmentStatus.EXPIRED, activeRound.getStatus());
        verify(repository).save(activeRound);
    }

    @Test
    void expireOutdatedRounds_doesNotUpdateIfStatusIsInactive() {
        InvestmentRound closedRound = new InvestmentRound();
        closedRound.setStatus(InvestmentStatus.CLOSED);
        closedRound.setEndDate(LocalDate.now().minusDays(5).toString());

        when(repository.findAll()).thenReturn(List.of(closedRound));

        service.expireOutdatedRounds();

        verify(repository, never()).save(any());
    }

    @Test
    void expireOutdatedRounds_doesNotExpireIfEndDateInFuture() {
        InvestmentRound futureRound = new InvestmentRound();
        futureRound.setStatus(InvestmentStatus.OPEN);
        futureRound.setEndDate(LocalDate.now().plusDays(1).toString());

        when(repository.findAll()).thenReturn(List.of(futureRound));

        service.expireOutdatedRounds();

        assertEquals(InvestmentStatus.OPEN, futureRound.getStatus());
        verify(repository, never()).save(any());
    }
}
