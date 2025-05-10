package ch.zhaw.fundhive.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.service.helpers.OwnershipService;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.investmentRound.InvestmentRoundService;
import ch.zhaw.fundhive.service.StartupService;

@WebMvcTest(InvestmentRoundController.class)
public class InvestmentRoundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OwnershipService ownerService;

    @MockBean
    private InvestmentRoundService roundService;

    @MockBean
    private StartupService startupService;

    @Test
    void createInvestmentRound_forbiddenIfNotEntrepreneur() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(false);

        mockMvc.perform(post("/api/investment-rounds")
                .with(jwt().jwt(jwt -> jwt.subject("user1")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                        {
                          "round_name": "Round A",
                          "goal_amount": 20000,
                          "date": "2025-01-01",
                          "startupId": "SU1"
                        }
                                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void createInvestmentRound_forbiddenIfNotOwner() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        when(ownerService.ownsStartup("SU1", "user1")).thenReturn(false);

        mockMvc.perform(post("/api/investment-rounds")
                .with(jwt().jwt(jwt -> jwt.subject("user1")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "round_name": "Round A",
                          "goal_amount": 20000,
                          "date": "2025-01-01",
                          "startupId": "SU1"
                        }

                                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void createInvestmentRound_success() throws Exception {
        InvestmentRound mockRound = new InvestmentRound();
        mockRound.setId("R1");

        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        when(ownerService.ownsStartup("SU1", "user1")).thenReturn(true);
        when(roundService.create(any())).thenReturn(mockRound);

        mockMvc.perform(post("/api/investment-rounds")
                .with(jwt().jwt(jwt -> jwt.subject("user1")))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "round_name": "Round A",
                          "goal_amount": 20000,
                          "date": "2025-01-01",
                          "startupId": "SU1"
                        }

                                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void getRoundsForStartup_returnsRoundsIfExists() throws Exception {
        when(startupService.startupExists("SU1")).thenReturn(true);

        InvestmentRound round = new InvestmentRound();
        round.setId("R1");

        when(roundService.getRoundsByStartupId("SU1")).thenReturn(List.of(round));

        mockMvc.perform(get("/api/investment-rounds/SU1")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getRoundsForStartup_returnsBadRequestIfStartupMissing() throws Exception {
        when(startupService.startupExists("SU2")).thenReturn(false);

        mockMvc.perform(get("/api/investment-rounds/SU2")
                .with(jwt()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllInvestmentRounds_forbiddenIfNotAdmin() throws Exception {
        when(userService.userHasRole("admin")).thenReturn(false);

        mockMvc.perform(get("/api/investment-rounds").with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllInvestmentRounds_returnsEmptyIfNoneMatch() throws Exception {
        when(userService.userHasRole("admin")).thenReturn(true);
        when(roundService.getAllInvestmentRounds(null, null, null, null, null))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/investment-rounds").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllInvestmentRounds_returnsFilteredRounds() throws Exception {
        when(userService.userHasRole("admin")).thenReturn(true);
        List<InvestmentRound> mockRounds = List.of(new InvestmentRound());
        when(roundService.getAllInvestmentRounds(
                any(), any(), any(), any(), any())).thenReturn(mockRounds);

        mockMvc.perform(get("/api/investment-rounds")
                .with(jwt())
                .param("minAmountRaised", "10000")
                .param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @ParameterizedTest
    @CsvSource({
            "10000,OPEN",
            "5000,CLOSED",
            "0,CANCELLED"
    })
    void getAllInvestmentRounds_returnsFilteredWithParams(Double minAmount, String statusStr) throws Exception {
        when(userService.userHasRole("admin")).thenReturn(true);
        List<InvestmentRound> mockRounds = List.of(new InvestmentRound());

        when(roundService.getAllInvestmentRounds(
                eq(minAmount), any(), any(), any(), eq(InvestmentStatus.valueOf(statusStr))))
                .thenReturn(mockRounds);

        mockMvc.perform(get("/api/investment-rounds")
                .with(jwt())
                .param("minAmountRaised", minAmount.toString())
                .param("status", statusStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

}
