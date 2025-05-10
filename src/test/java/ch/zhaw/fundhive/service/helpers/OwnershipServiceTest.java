package ch.zhaw.fundhive.service.helpers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OwnershipServiceTest {

    private StartupRepository startupRepo;
    private InvestmentRoundRepository roundRepo;
    private OwnershipService service;

    @BeforeEach
    void setup() {
        startupRepo = mock(StartupRepository.class);
        roundRepo = mock(InvestmentRoundRepository.class);
        service = new OwnershipService();

        // Inject mocks using reflection
        org.springframework.test.util.ReflectionTestUtils.setField(service, "startupRepo", startupRepo);
        org.springframework.test.util.ReflectionTestUtils.setField(service, "repository", roundRepo);
    }

    @Test
    void ownsStartup_returnsTrueIfOwner() {
        Startup startup = new Startup();
        startup.setOwnerId("user1");
        when(startupRepo.findById("SU1")).thenReturn(Optional.of(startup));

        assertTrue(service.ownsStartup("SU1", "user1"));
    }

    @Test
    void ownsStartup_returnsFalseIfNotOwner() {
        Startup startup = new Startup();
        startup.setOwnerId("someone-else");
        when(startupRepo.findById("SU1")).thenReturn(Optional.of(startup));

        assertFalse(service.ownsStartup("SU1", "user1"));
    }

    @Test
    void ownsStartup_returnsFalseIfStartupMissing() {
        when(startupRepo.findById("SU1")).thenReturn(Optional.empty());

        assertFalse(service.ownsStartup("SU1", "user1"));
    }

    @Test
    void ownsRound_returnsTrueIfOwner() {
        InvestmentRound round = new InvestmentRound();
        round.setStartupId("SU1");
        Startup startup = new Startup();
        startup.setOwnerId("user1");

        when(roundRepo.findById("R1")).thenReturn(Optional.of(round));
        when(startupRepo.findById("SU1")).thenReturn(Optional.of(startup));

        assertTrue(service.ownsRound("R1", "user1"));
    }

    @Test
    void ownsRound_returnsFalseIfNotOwner() {
        InvestmentRound round = new InvestmentRound();
        round.setStartupId("SU1");
        Startup startup = new Startup();
        startup.setOwnerId("someone-else");

        when(roundRepo.findById("R1")).thenReturn(Optional.of(round));
        when(startupRepo.findById("SU1")).thenReturn(Optional.of(startup));

        assertFalse(service.ownsRound("R1", "user1"));
    }

    @Test
    void ownsRound_returnsFalseIfRoundOrStartupMissing() {
        when(roundRepo.findById("R1")).thenReturn(Optional.empty());
        assertFalse(service.ownsRound("R1", "user1"));

        InvestmentRound round = new InvestmentRound();
        round.setStartupId("SU1");

        when(roundRepo.findById("R1")).thenReturn(Optional.of(round));
        when(startupRepo.findById("SU1")).thenReturn(Optional.empty());
        assertFalse(service.ownsRound("R1", "user1"));
    }
}
