package ch.zhaw.fundhive.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.service.helpers.OwnershipService;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.investmentRound.InvestmentRoundStatusService;

@WebMvcTest(InvestmentRoundStatusServiceController.class)
public class InvestmentRoundStatusServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private OwnershipService ownerService;

    @MockBean
    private InvestmentRoundStatusService statusService;

    @Test
    void cancelRound_forbiddenIfNotEntrepreneur() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(false);

        mockMvc.perform(put("/api/investment-rounds/R1/cancel")
                .with(jwt().jwt(jwt -> jwt.subject("user1"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void cancelRound_forbiddenIfNotOwner() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        when(ownerService.ownsRound("R1", "user1")).thenReturn(false);

        mockMvc.perform(put("/api/investment-rounds/R1/cancel")
                .with(jwt().jwt(jwt -> jwt.subject("user1"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void cancelRound_success() throws Exception {
        InvestmentRound mockRound = new InvestmentRound();
        mockRound.setId("R1"); // Ensure this is set to match controller expectations

        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        when(ownerService.ownsRound("R1", "user1")).thenReturn(true);
        when(statusService.cancelRound("R1")).thenReturn(Optional.of(mockRound));

        mockMvc.perform(put("/api/investment-rounds/R1/cancel")
                .with(jwt().jwt(jwt -> jwt.subject("user1"))))
                .andExpect(status().isOk());
    }

    @Test
    void cancelRound_returnsBadRequestIfFailed() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        when(ownerService.ownsRound("R1", "user1")).thenReturn(true);
        when(statusService.cancelRound("R1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/investment-rounds/R1/cancel")
                .with(jwt().jwt(jwt -> jwt.subject("user1"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void openRound_forbiddenIfNotEntrepreneur() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(false);

        mockMvc.perform(put("/api/investment-rounds/R1/open")
                .with(jwt().jwt(jwt -> jwt.subject("user1"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void openRound_forbiddenIfNotOwner() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        when(ownerService.ownsRound("R1", "user1")).thenReturn(false);

        mockMvc.perform(put("/api/investment-rounds/R1/open")
                .with(jwt().jwt(jwt -> jwt.subject("user1"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void openRound_success() throws Exception {
        InvestmentRound mockRound = new InvestmentRound();
        mockRound.setId("R1");

        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        when(ownerService.ownsRound("R1", "user1")).thenReturn(true);
        when(statusService.openRound("R1")).thenReturn(Optional.of(mockRound));

        mockMvc.perform(put("/api/investment-rounds/R1/open")
                .with(jwt().jwt(jwt -> jwt.subject("user1"))))
                .andExpect(status().isOk());
    }

    @Test
    void openRound_returnsBadRequestIfFailed() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        when(ownerService.ownsRound("R1", "user1")).thenReturn(true);
        when(statusService.openRound("R1")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/investment-rounds/R1/open")
                .with(jwt().jwt(jwt -> jwt.subject("user1"))))
                .andExpect(status().isBadRequest());
    }

}
