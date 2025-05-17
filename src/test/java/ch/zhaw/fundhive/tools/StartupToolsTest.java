package ch.zhaw.fundhive.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.repository.StartupRepository;
import ch.zhaw.fundhive.service.StartupService;

@ExtendWith(MockitoExtension.class)
class StartupToolsTest {

    @Mock
    private StartupRepository startupRepository;

    @Mock
    private StartupService startupService;

    @InjectMocks
    private StartupTools startupTools;

    @Test
    void getAllStartups_returnsAll() {
        // Arrange
        List<Startup> mockList = List.of(new Startup(), new Startup());
        when(startupRepository.findAll()).thenReturn(mockList);

        // Act
        List<Startup> result = startupTools.getAllStartups();

        // Assert
        assertEquals(2, result.size());
        verify(startupRepository).findAll();
    }

    @Test
    void filterStartups_withAllNulls_returnsAll() {
        // Arrange
        List<Startup> mockList = List.of(new Startup());
        when(startupService.filterStartups(
                isNull(), isNull(), isNull(), isNull(), isNull(), isNull())).thenReturn(mockList);

        // Act
        List<Startup> result = startupTools.filterStartups(null, null, null, null, null, null);

        // Assert
        assertEquals(1, result.size());
        verify(startupService).filterStartups(null, null, null, null, null, null);
    }

    @Test
    void filterStartups_withValues_convertsEnumsCorrectly() {
        // Arrange
        String industry = "TECH";
        String funding = "SEED";
        Double minVal = 1_000_000.0;
        Double maxVal = 10_000_000.0;
        String name = "Test";
        Double aiRating = 4.5;

        IndustryType expectedIndustry = IndustryType.valueOf(industry);
        StartupFundingStatus expectedFunding = StartupFundingStatus.valueOf(funding);

        List<Startup> mockList = List.of(new Startup());
        when(startupService.filterStartups(
                expectedIndustry, expectedFunding, minVal, maxVal, name, aiRating)).thenReturn(mockList);

        // Act
        List<Startup> result = startupTools.filterStartups(
                industry, funding, minVal, maxVal, name, aiRating);

        // Assert
        assertEquals(1, result.size());
        verify(startupService).filterStartups(
                expectedIndustry, expectedFunding, minVal, maxVal, name, aiRating);
    }
}
