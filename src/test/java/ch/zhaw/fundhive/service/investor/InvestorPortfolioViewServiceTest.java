package ch.zhaw.fundhive.service.investor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.zhaw.fundhive.model.dto.InvestmentAllTransactionsDTO;
import ch.zhaw.fundhive.model.dto.InvestorPortfolioDTO;
import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import ch.zhaw.fundhive.service.helpers.ResolveHelperService;

@ExtendWith(MockitoExtension.class)
class InvestorPortfolioViewServiceTest {

    @Mock
    private InvestmentTransactionRepository transactionRepo;

    @Mock
    private ResolveHelperService resolveHelperService;

    @InjectMocks
    private InvestorPortfolioViewService service;

    @Test
    void getInvestorPortfolio_returnsCorrectPortfolio() {
        // Arrange
        String investorId = "INV1";
        String roundId = "R1";
        String startupId = "SU1";

        InvestmentTransaction tx = new InvestmentTransaction();
        tx.setId("T1");
        tx.setInvestmentRoundId(roundId);
        tx.setAmount(2500.0);
        tx.setDate("2025-01-01");

        List<InvestmentTransaction> transactions = List.of(tx);
        when(transactionRepo.findByInvestorId(investorId)).thenReturn(transactions);

        when(resolveHelperService.resolveRoundName(roundId)).thenReturn("Seed Round");
        when(resolveHelperService.resolveStartupId(roundId)).thenReturn(startupId);
        when(resolveHelperService.resolveStartupName(startupId)).thenReturn("Tech Corp");
        when(resolveHelperService.resolveInvestorName(investorId)).thenReturn("Jane Doe");

        // Act
        InvestorPortfolioDTO portfolio = service.getInvestorPortfolio(investorId);

        // Assert
        assertEquals(2500.0, portfolio.getTotalAmount());
        assertEquals(1, portfolio.getTransactionCount());

        InvestmentAllTransactionsDTO dto = portfolio.getTransactions().get(0);
        assertEquals("T1", dto.getTransactionId());
        assertEquals(investorId, dto.getInvestorId());
        assertEquals("Jane Doe", dto.getInvestorName());
        assertEquals(roundId, dto.getRoundId());
        assertEquals("Seed Round", dto.getRoundName());
        assertEquals(startupId, dto.getStartupId());
        assertEquals("Tech Corp", dto.getStartupName());
        assertEquals(2500.0, dto.getAmount());
        assertEquals("2025-01-01", dto.getDate());
    }

    @Test
    void getInvestorPortfolio_aggregatesMultipleTransactionsCorrectly() {
        String investorId = "INV1";
        String roundId = "R1";
        String startupId = "SU1";

        InvestmentTransaction tx1 = new InvestmentTransaction();
        tx1.setId("T1");
        tx1.setInvestmentRoundId(roundId);
        tx1.setAmount(1000.0);
        tx1.setDate("2025-01-01");

        InvestmentTransaction tx2 = new InvestmentTransaction();
        tx2.setId("T2");
        tx2.setInvestmentRoundId(roundId);
        tx2.setAmount(1500.0);
        tx2.setDate("2025-01-02");

        when(transactionRepo.findByInvestorId(investorId)).thenReturn(List.of(tx1, tx2));
        when(resolveHelperService.resolveRoundName(roundId)).thenReturn("Seed Round");
        when(resolveHelperService.resolveStartupId(roundId)).thenReturn(startupId);
        when(resolveHelperService.resolveStartupName(startupId)).thenReturn("Tech Corp");
        when(resolveHelperService.resolveInvestorName(investorId)).thenReturn("Jane Doe");

        InvestorPortfolioDTO portfolio = service.getInvestorPortfolio(investorId);

        assertEquals(2500.0, portfolio.getTotalAmount());
        assertEquals(2, portfolio.getTransactionCount());
        assertEquals(2, portfolio.getTransactions().size());
    }

    @Test
    void getInvestorPortfolio_returnsEmptyPortfolioIfNoTransactions() {
        String investorId = "INV1";

        when(transactionRepo.findByInvestorId(investorId)).thenReturn(List.of());

        InvestorPortfolioDTO portfolio = service.getInvestorPortfolio(investorId);

        assertEquals(0.0, portfolio.getTotalAmount());
        assertEquals(0, portfolio.getTransactionCount());
        assertTrue(portfolio.getTransactions().isEmpty());
    }

}
