package ch.zhaw.fundhive.service.ai;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.repository.StartupRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StartupAiRatingServiceTest {

    @Mock
    private StartupRepository startupRepo;

    @Mock
    private OpenAiChatModel chatModel;

    @InjectMocks
    private StartupAiRatingService ratingService;

    private Startup sample;

    @BeforeEach
    void setUp() {
        sample = new Startup();
        sample.setName("TestCo");
        sample.setIndustry(IndustryType.TECH);
        sample.setValuation(1_000_000.0);
        sample.setDescription("Desc");
        sample.setFundingStatus(StartupFundingStatus.SEED);
        sample.setOwnerId("owner");
    }

    /*
     * // Uncomment this test to run the scheduled logic once
     * 
     * @Autowired
     * private StartupAiRatingService ratingSvc;
     * 
     * @Autowired
     * private StartupRepository repo;
     * 
     * @Test
     * void runRatingOnce() {
     * ratingSvc.rateAllStartups();
     * // print out or assert that aiRating is now non-null
     * repo.findAll().forEach(s -> System.out.println(s.getName() + " → " +
     * s.getAiRating()));
     * }
     */

    @Test
    void rateStartupOnCreation_returnsParsedRating_whenLLMReturnsNumeric() {
        // stub the LLM to return a valid decimal
        when(chatModel.call(anyString())).thenReturn("3.7");

        String rating = ratingService.rateStartupOnCreation(sample);

        assertEquals("3.7", rating);
    }

    @Test
    void rateStartupOnCreation_fallsBackToZero_whenLLMReturnsInvalid() {
        // stub the LLM to return non-numeric
        when(chatModel.call(anyString())).thenReturn("not-a-number");

        String rating = ratingService.rateStartupOnCreation(sample);

        assertEquals("0.0", rating);
    }

    @Test
    void rateStartupOnCreation_clampsAtBounds() {
        // above 5.0
        when(chatModel.call(anyString())).thenReturn("9.9");
        assertEquals("5.0", ratingService.rateStartupOnCreation(sample));

        // below 0.0
        when(chatModel.call(anyString())).thenReturn("-3.2");
        assertEquals("0.0", ratingService.rateStartupOnCreation(sample));
    }

    @Test
    void testRateAllStartups_SuccessfulRating() {
        // Arrange: one startup with default aiRating "0.0"
        Startup s = new Startup();
        s.setId("s1");
        s.setName("Acme Rockets");
        s.setIndustry(IndustryType.TECH);
        s.setValuation(1_000_000.0);
        s.setDescription("We build rockets.");
        s.setFundingStatus(StartupFundingStatus.SEED);
        s.setOwnerId("owner1");
        s.setAiRating("0.0");

        when(startupRepo.findAll()).thenReturn(List.of(s));
        // Stub the LLM call to return a valid rating
        when(chatModel.call(anyString())).thenReturn("4.2");

        // Act
        ratingService.rateAllStartups();

        // Assert: in-memory update
        assertEquals("4.2", s.getAiRating());

        // Verify persistence
        verify(startupRepo).saveAll(argThat(iterable -> {
            List<Startup> list;
            if (iterable instanceof List) {
                list = (List<Startup>) iterable;
            } else {
                list = new java.util.ArrayList<>();
                iterable.forEach(list::add);
            }
            return list.size() == 1 && "4.2".equals(list.get(0).getAiRating());
        }));
    }

    @Test
    void testRateAllStartups_FallbackOnInvalidRating() {
        // Arrange: one startup with default aiRating "0.0"
        Startup s = new Startup();
        s.setId("s2");
        s.setName("Beta Builders");
        s.setIndustry(IndustryType.TECH); // Use an existing enum value, replace with appropriate value if needed
        s.setValuation(500_000.0);
        s.setDescription("Prototype platform.");
        s.setFundingStatus(StartupFundingStatus.PRE_SEED);
        s.setOwnerId("owner2");
        s.setAiRating("0.0");

        when(startupRepo.findAll()).thenReturn(List.of(s));
        // Stub the LLM call to return a non-numeric string
        when(chatModel.call(anyString())).thenReturn("invalid");

        // Act
        ratingService.rateAllStartups();

        // Assert: fallback to 0.0 on parse error
        assertEquals("0.0", s.getAiRating());

        // Verify persistence
        verify(startupRepo).saveAll(argThat(iterable -> {
            List<Startup> list;
            if (iterable instanceof List) {
                list = (List<Startup>) iterable;
            } else {
                list = new java.util.ArrayList<>();
                iterable.forEach(list::add);
            }
            return list.size() == 1 && "0.0".equals(list.get(0).getAiRating());
        }));
    }

}
