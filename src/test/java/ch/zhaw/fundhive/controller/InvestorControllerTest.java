package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.service.investor.InvestorService;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.investor.InvestorUpsertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvestorController.class)
class InvestorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvestorService service;

    @MockBean
    private UserService userService;

    @MockBean
    private InvestorUpsertService upsertService;

    private final String investorJson = """
            {
                "id": "inv1",
                "name": "Jane Doe",
                "email": "jane@example.com"
            }
            """;

    @Test
    void getAllInvestors_forbiddenIfNotAdmin() throws Exception {
        when(userService.userHasRole("admin")).thenReturn(false);

        mockMvc.perform(get("/api/investors").with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllInvestors_returnsListIfAdmin() throws Exception {
        when(userService.userHasRole("admin")).thenReturn(true);
        when(service.getAllInvestors()).thenReturn(List.of(new Investor()));

        mockMvc.perform(get("/api/investors").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void upsertInvestor_forbiddenIfNotInvestor() throws Exception {
        when(userService.userHasRole("investor")).thenReturn(false);

        mockMvc.perform(post("/api/investors/me").with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void upsertInvestor_successIfInvestor() throws Exception {
        when(userService.userHasRole("investor")).thenReturn(true);

        mockMvc.perform(post("/api/investors/me").with(jwt()))
                .andExpect(status().isCreated());
    }

    @Test
    void createInvestor_returnsCreated() throws Exception {
        Investor saved = new Investor();
        saved.setId("inv1");
        saved.setName("Jane Doe");
        saved.setEmail("jane@example.com");
        when(service.createInvestor(any(Investor.class))).thenReturn(saved);

        mockMvc.perform(post("/api/investors")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(investorJson))
                .andExpect(status().isCreated());
    }

    @Test
    void updateInvestor_returnsUpdated() throws Exception {
        Investor updated = new Investor();
        updated.setId("inv1");
        updated.setName("Jane Doe Updated");
        updated.setEmail("jane.updated@example.com");
        when(service.updateInvestor(eq("inv1"), any(Investor.class))).thenReturn(updated);

        mockMvc.perform(put("/api/investors/inv1")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(investorJson))
                .andExpect(status().isOk());
    }
}
