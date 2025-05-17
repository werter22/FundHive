package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.service.StartupService;
import ch.zhaw.fundhive.service.helpers.OwnershipService;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(StartupController.class)
class StartupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StartupService startupService;

    @MockBean
    private UserService userService;

    @MockBean
    private OwnershipService ownerService;

    @Test
    void createStartup_returnsCreatedIfEntrepreneur() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("123");

        Startup mockStartup = new Startup();
        mockStartup.setId("startup123");
        when(startupService.create(any())).thenReturn(mockStartup);

        String json = """
                {
                    "name": "Test Startup",
                    "description": "Description",
                    "industry": "TECH",
                    "valuation": 1000000,
                    "fundingStatus": "SEED"
                }
                """;

        mockMvc.perform(post("/api/startups")
                .with(jwt().authorities(() -> "entrepreneur"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createStartup_returnsForbiddenIfNotEntrepreneur() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(false);

        String json = """
                    {
                        "name": "Fail Startup",
                        "description": "Desc",
                        "industry": "TECH",
                        "valuation": 500000,
                        "fundingStatus": "SEED"
                    }
                """;

        mockMvc.perform(post("/api/startups")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void getStartup_returnsStartupIfExists() throws Exception {
        Startup mockStartup = new Startup();
        mockStartup.setId("SU1");
        mockStartup.setName("Mock Startup");

        when(startupService.getStartupById("SU1")).thenReturn(java.util.Optional.of(mockStartup));

        mockMvc.perform(get("/api/startups/SU1")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("SU1"))
                .andExpect(jsonPath("$.name").value("Mock Startup"));
    }

    @Test
    void getStartup_returnsNotFoundIfMissing() throws Exception {
        when(startupService.getStartupById("SU2")).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/startups/SU2")
                .with(jwt()))
                .andExpect(status().isNotFound());
    }

    private final String startupJson = """
            {
                "id": "SU1",
                "name": "Updated Startup",
                "description": "New desc",
                "valuation": 1000000,
                "industry": "TECH",
                "fundingStatus": "SEED",
                "ownerId": "user1"
            }
            """;

    @Test
    void updateStartup_forbiddenIfNotEntrepreneur() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(false);

        mockMvc.perform(put("/api/startups/SU1")
                .with(jwt().jwt(jwt -> jwt.subject("user1")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(startupJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateStartup_forbiddenIfNotOwner() throws Exception {
        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user2"); // not owner
        when(ownerService.ownsStartup("user1", "user2")).thenReturn(false);

        mockMvc.perform(put("/api/startups/SU1")
                .with(jwt().jwt(jwt -> jwt.subject("user2")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(startupJson))
                .andExpect(status().isForbidden());
    }

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void updateStartup_returnsOkIfEntrepreneurAndOwner() throws Exception {
        // 1) prepare a dummy Startup JSON payload
        Startup updatedStartup = new Startup();
        updatedStartup.setName("MyCoolStartup");
        updatedStartup.setDescription("<p>New HTML</p>");
        updatedStartup.setIndustry(IndustryType.TECH);
        updatedStartup.setValuation(1234.0);
        updatedStartup.setFundingStatus(StartupFundingStatus.SEED);
        updatedStartup.setOwnerId("user1");

        String startupJson = mapper.writeValueAsString(updatedStartup);

        // 2) stub your security & ownership checks
        when(userService.userHasRole("entrepreneur")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user1");
        // NOTE: the first arg is the path‐variable id ("SU1"), second is userId
        when(ownerService.ownsStartup("SU1", "user1")).thenReturn(true);
        // stub the service method itself
        when(startupService.updateStartup(eq("SU1"), any(Startup.class)))
                .thenReturn(updatedStartup);

        // 3) perform the PUT
        mockMvc.perform(put("/api/startups/SU1")
                .with(jwt().jwt(jwt -> jwt.subject("user1")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(startupJson))
                .andExpect(status().isOk())
                // optional: verify body round‐trips
                .andExpect(jsonPath("$.name").value("MyCoolStartup"))
                .andExpect(jsonPath("$.description").value("<p>New HTML</p>"));

        // 4) verify we actually called through to the service with the right args
        verify(startupService).updateStartup(eq("SU1"), argThat(s -> "MyCoolStartup".equals(s.getName()) &&
                "<p>New HTML</p>".equals(s.getDescription()) &&
                "user1".equals(s.getOwnerId())));
    }

    @Test
    void getFilteredStartups_returnsFilteredList() throws Exception {
        when(startupService.filterStartups(any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(new Startup()));

        mockMvc.perform(get("/api/startups")
                .param("industry", "TECH")
                .param("minValuation", "100000")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getFilteredStartups_returnsEmptyListIfNoneFound() throws Exception {
        when(startupService.filterStartups(any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/startups")
                .param("industry", "MEDIA")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getFundingOverview_returnsOverviewIfFound() throws Exception {
        FundingOverviewDTO dto = new FundingOverviewDTO("SU1", 150000.0, 3);
        when(startupService.getFundingOverview("SU1")).thenReturn(dto);

        mockMvc.perform(get("/api/startups/SU1/funding-overview")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getFundingOverview_returnsNotFoundIfMissing() throws Exception {
        when(startupService.getFundingOverview("SU2")).thenReturn(null);

        mockMvc.perform(get("/api/startups/SU2/funding-overview")
                .with(jwt()))
                .andExpect(status().isNotFound());
    }

}
