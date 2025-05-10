package ch.zhaw.fundhive.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.dto.InvestmentAllTransactionsDTO;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.InvestmentTransactionService;

@WebMvcTest(InvestmentTransactionController.class)
public class InvestmentTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private InvestmentTransactionService transactionService;

    @Test
    void createTransaction_forbiddenIfNotInvestor() throws Exception {
        when(userService.userHasRole("investor")).thenReturn(false);

        mockMvc.perform(post("/api/investment-transactions")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"investmentRoundId\":\"R1\",\"amount\":500}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createTransaction_returnsOkWhenValid() throws Exception {
        when(userService.userHasRole("investor")).thenReturn(true);
        InvestmentTransaction tx = new InvestmentTransaction();
        when(transactionService.create(any())).thenReturn(Optional.of(tx));

        mockMvc.perform(post("/api/investment-transactions")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"investmentRoundId\":\"R1\",\"amount\":500}"))
                .andExpect(status().isOk());
    }

    @Test
    void getTransactions_forbiddenIfNotAdmin() throws Exception {
        when(userService.userHasRole("admin")).thenReturn(false);

        mockMvc.perform(get("/api/investment-transactions").with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getTransactions_returnsListIfAdmin() throws Exception {
        when(userService.userHasRole("admin")).thenReturn(true);
        when(transactionService.getFilteredTransactionsForAdmin(
                any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/investment-transactions").with(jwt()))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "inv1, SU1, R1, 100.0, 5000.0, 2024-01-01, 2024-12-31",
            "inv2, , R2, , , , ",
            ", SU2, , 1000.0, , , ",
            ", , , , , , " // all null to simulate no filter
    })
    void getFilteredTransactions_returnsResultsForAdmin(
            String investorId,
            String startupId,
            String roundId,
            Double minAmount,
            Double maxAmount,
            String startDate,
            String endDate) throws Exception {
        when(userService.userHasRole("admin")).thenReturn(true);
        InvestmentAllTransactionsDTO dto = org.mockito.Mockito.mock(InvestmentAllTransactionsDTO.class);
        List<InvestmentAllTransactionsDTO> mockResults = java.util.Collections.singletonList(dto);

        when(transactionService.getFilteredTransactionsForAdmin(
                eq(investorId),
                eq(startupId),
                eq(roundId),
                eq(minAmount),
                eq(maxAmount),
                startDate != null ? eq(LocalDate.parse(startDate)) : isNull(),
                endDate != null ? eq(LocalDate.parse(endDate)) : isNull())).thenReturn(mockResults);

        var request = get("/api/investment-transactions").with(jwt());

        if (investorId != null)
            request = request.param("investorId", investorId);
        if (startupId != null)
            request = request.param("startupId", startupId);
        if (roundId != null)
            request = request.param("roundId", roundId);
        if (minAmount != null)
            request = request.param("minAmount", minAmount.toString());
        if (maxAmount != null)
            request = request.param("maxAmount", maxAmount.toString());
        if (startDate != null)
            request = request.param("startDate", startDate);
        if (endDate != null)
            request = request.param("endDate", endDate);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

}
