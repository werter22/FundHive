package ch.zhaw.fundhive.service.helpers;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.InvestorRepository;
import ch.zhaw.fundhive.repository.StartupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResolveHelperServiceTest {

    private InvestmentRoundRepository investmentRoundRepo;
    private InvestorRepository investorRepo;
    private StartupRepository startupRepo;
    private ResolveHelperService helper;

    @BeforeEach
    void setup() {
        investmentRoundRepo = mock(InvestmentRoundRepository.class);
        investorRepo = mock(InvestorRepository.class);
        startupRepo = mock(StartupRepository.class);

        helper = new ResolveHelperService();
        ReflectionTestUtils.setField(helper, "investmentRoundRepository", investmentRoundRepo);
        ReflectionTestUtils.setField(helper, "investorRepository", investorRepo);
        ReflectionTestUtils.setField(helper, "startupRepository", startupRepo);
    }

    @Test
    void resolveStartupId_returnsIdIfExists() {
        var round = new InvestmentRound();
        round.setStartupId("SU1");
        when(investmentRoundRepo.findById("R1")).thenReturn(Optional.of(round));

        String result = helper.resolveStartupId("R1");
        assertEquals("SU1", result);
    }

    @Test
    void resolveStartupId_returnsUnknownIfMissing() {
        when(investmentRoundRepo.findById("R1")).thenReturn(Optional.empty());

        String result = helper.resolveStartupId("R1");
        assertEquals("unknown-startup", result);
    }

    @Test
    void resolveRoundName_returnsNameIfExists() {
        var round = new InvestmentRound();
        round.setRound_name("Series A");
        when(investmentRoundRepo.findById("R1")).thenReturn(Optional.of(round));

        String result = helper.resolveRoundName("R1");
        assertEquals("Series A", result);
    }

    @Test
    void resolveInvestorName_returnsNameIfExists() {
        var investor = new Investor();
        investor.setName("Alice");
        when(investorRepo.findById("I1")).thenReturn(Optional.of(investor));

        String result = helper.resolveInvestorName("I1");
        assertEquals("Alice", result);
    }

    @Test
    void resolveStartupName_returnsNameIfExists() {
        var startup = new Startup();
        startup.setName("TechX");
        when(startupRepo.findById("SU1")).thenReturn(Optional.of(startup));

        String result = helper.resolveStartupName("SU1");
        assertEquals("TechX", result);
    }

    @Test
    void resolveStartupName_returnsUnknownIfMissing() {
        when(startupRepo.findById("SU1")).thenReturn(Optional.empty());

        String result = helper.resolveStartupName("SU1");
        assertEquals("unknown-startup", result);
    }
}
