package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.dto.InvestorPortfolioDTO;
import ch.zhaw.fundhive.service.investor.InvestorPortfolioViewService;
import ch.zhaw.fundhive.service.helpers.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(InvestorPortfolioViewController.class)
class InvestorPortfolioViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvestorPortfolioViewService portfolioService;

    @MockBean
    private UserService userService;

    @Test
    void getPortfolio_returnsForbiddenIfNotInvestor() throws Exception {
        when(userService.userHasRole("investor")).thenReturn(false);

        mockMvc.perform(get("/api/investment-transactions/123/portfolio")
                .with(jwt().jwt(jwt -> jwt.subject("123"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getPortfolio_returnsForbiddenIfUserIdMismatch() throws Exception {
        when(userService.userHasRole("investor")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("abc"); // mismatch

        mockMvc.perform(get("/api/investment-transactions/xyz/portfolio")
                .with(jwt().jwt(jwt -> jwt.subject("xyz"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getPortfolio_returnsPortfolioIfAuthorized() throws Exception {
        InvestorPortfolioDTO dto = new InvestorPortfolioDTO(10000.0, 3, List.of());

        when(userService.userHasRole("investor")).thenReturn(true);
        when(userService.getCurrentUserId()).thenReturn("user123");
        when(portfolioService.getInvestorPortfolio("user123")).thenReturn(dto);

        mockMvc.perform(get("/api/investment-transactions/user123/portfolio")
                .with(jwt().jwt(jwt -> jwt.subject("user123"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
