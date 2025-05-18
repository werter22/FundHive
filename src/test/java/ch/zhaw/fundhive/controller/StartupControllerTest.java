package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.service.StartupService;
import ch.zhaw.fundhive.service.helpers.OwnershipService;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;
import ch.zhaw.fundhive.model.dto.StartupUpdateDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;

import org.junit.jupiter.api.Test;
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
                // 1) Prepare a dummy StartupUpdateDTO JSON payload
                StartupUpdateDTO dto = new StartupUpdateDTO();
                dto.setName("MyCoolStartup");
                dto.setDescription("<p>New HTML</p>");
                dto.setIndustry(IndustryType.TECH);
                dto.setValuation(1234.0);
                dto.setFundingStatus(StartupFundingStatus.SEED);

                String dtoJson = mapper.writeValueAsString(dto);

                // 2) Stub your security & ownership checks
                when(userService.userHasRole("entrepreneur")).thenReturn(true);
                when(userService.getCurrentUserId()).thenReturn("user1");
                when(ownerService.ownsStartup("SU1", "user1")).thenReturn(true);

                // 3) Stub the service method itself
                Startup returned = new Startup();
                returned.setId("SU1");
                returned.setName(dto.getName());
                returned.setDescription(dto.getDescription());
                returned.setIndustry(dto.getIndustry());
                returned.setValuation(dto.getValuation());
                returned.setFundingStatus(dto.getFundingStatus());
                returned.setOwnerId("user1");
                when(startupService.updateStartup(eq("SU1"), any(StartupUpdateDTO.class)))
                                .thenReturn(returned);

                // 4) Perform the PUT
                mockMvc.perform(put("/api/startups/{id}", "SU1")
                                .with(jwt().jwt(jwt -> jwt
                                                .claim("sub", "user1")
                                                .claim("user_roles", List.of("entrepreneur"))))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoJson))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value("SU1"))
                                .andExpect(jsonPath("$.name").value("MyCoolStartup"))
                                .andExpect(jsonPath("$.description").value("<p>New HTML</p>"))
                                .andExpect(jsonPath("$.industry").value("TECH"))
                                .andExpect(jsonPath("$.valuation").value(1234.0))
                                .andExpect(jsonPath("$.fundingStatus").value("SEED"));

                // 5) Verify that the service was called with exactly our DTO
                verify(startupService).updateStartup(
                                eq("SU1"),
                                argThat(d -> "MyCoolStartup".equals(d.getName()) &&
                                                "<p>New HTML</p>".equals(d.getDescription()) &&
                                                IndustryType.TECH == d.getIndustry() &&
                                                Double.valueOf(1234.0).equals(d.getValuation()) &&
                                                StartupFundingStatus.SEED == d.getFundingStatus()));
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
