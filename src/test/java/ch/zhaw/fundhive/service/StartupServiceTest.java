package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;
import ch.zhaw.fundhive.model.dto.StartupCreateDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.repository.StartupRepository;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.service.helpers.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class StartupServiceTest {

    @Mock
    private StartupRepository startupRepository;

    @Mock
    private InvestmentRoundRepository investmentRoundRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private StartupService startupService;

    @Test
    void create_shouldPopulateAndSaveStartup() {
        // Arrange
        StartupCreateDTO dto = new StartupCreateDTO();
        dto.setName("MyStartup");
        dto.setDescription("Innovative tech company");
        dto.setIndustry(IndustryType.TECH);
        dto.setValuation(1_000_000.0);
        dto.setFundingStatus(StartupFundingStatus.SEED);

        when(userService.getCurrentUserId()).thenReturn("USR1");

        Startup saved = new Startup();
        when(startupRepository.save(any())).thenReturn(saved);

        // Act
        Startup result = startupService.create(dto);

        // Assert
        assertEquals(saved, result);
        verify(startupRepository).save(any());
    }

    @Test
    void getStartupById_returnsStartupIfExists() {
        Startup startup = new Startup();
        startup.setId("SU1");

        when(startupRepository.findById("SU1")).thenReturn(Optional.of(startup));

        Optional<Startup> result = startupService.getStartupById("SU1");

        assertTrue(result.isPresent());
        assertEquals("SU1", result.get().getId());
        verify(startupRepository).findById("SU1");
    }

    @Test
    void updateStartup_updatesAndSavesFields() {
        Startup existing = new Startup();
        existing.setId("SU1");

        Startup incoming = new Startup();
        incoming.setName("Updated");
        incoming.setDescription("New Desc");
        incoming.setIndustry(IndustryType.TECH);
        incoming.setValuation(1_500_000.0);
        incoming.setFundingStatus(StartupFundingStatus.SEED);
        incoming.setAiRating("4.7");

        when(startupRepository.findById("SU1")).thenReturn(Optional.of(existing));
        when(startupRepository.save(existing)).thenReturn(existing);

        Startup result = startupService.updateStartup("SU1", incoming);

        assertEquals("Updated", result.getName());
        assertEquals("New Desc", result.getDescription());
        assertEquals(IndustryType.TECH, result.getIndustry());
        assertEquals(1_500_000.0, result.getValuation());
        assertEquals(StartupFundingStatus.SEED, result.getFundingStatus());
        assertEquals("4.7", result.getAiRating());

        verify(startupRepository).save(existing);
    }

    @Test
    void filterStartups_appliesAllFilters() {
        Startup s1 = new Startup();
        s1.setName("Startup 1");
        s1.setDescription("Desc");
        s1.setIndustry(IndustryType.TECH);
        s1.setValuation(1000.0);
        s1.setFundingStatus(StartupFundingStatus.SEED);
        s1.setAiRating("4.0");

        Startup s2 = new Startup();
        s2.setName("Startup 2");
        s2.setDescription("Desc");
        s2.setIndustry(IndustryType.FINTECH);
        s2.setValuation(5000.0);
        s2.setFundingStatus(StartupFundingStatus.SERIES_A);
        s2.setAiRating("4.5");

        when(startupRepository.findAll()).thenReturn(List.of(s1, s2));

        List<Startup> result = startupService.filterStartups(
                IndustryType.TECH,
                StartupFundingStatus.SEED,
                500.0,
                2000.0,
                "startup",
                3.0);

        assertEquals(1, result.size());
        assertEquals("Startup 1", result.get(0).getName());
    }

    @Test
    void filterStartups_returnsAllWhenNoFilters() {
        Startup s1 = new Startup(); // mock or set relevant values if needed
        Startup s2 = new Startup();

        when(startupRepository.findAll()).thenReturn(List.of(s1, s2));

        List<Startup> result = startupService.filterStartups(null, null, null, null, null, null);

        assertEquals(2, result.size());
    }

    @Test
    void getFundingOverview_delegatesToRepository() {
        FundingOverviewDTO dto = mock(FundingOverviewDTO.class);
        when(investmentRoundRepository.getFundingOverview("SU1")).thenReturn(dto);

        FundingOverviewDTO result = startupService.getFundingOverview("SU1");

        assertEquals(dto, result);
        verify(investmentRoundRepository).getFundingOverview("SU1");
    }

    @Test
    void startupExists_returnsTrueIfExists() {
        when(startupRepository.existsById("SU1")).thenReturn(true);

        assertTrue(startupService.startupExists("SU1"));
    }

}
