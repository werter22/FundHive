package ch.zhaw.fundhive.service.ai;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;

import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import ch.zhaw.fundhive.repository.InvestorRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InvestorMockAiRatingServiceTest {

    @Mock
    InvestorRepository investorRepo;
    @Mock
    InvestmentTransactionRepository txRepo;
    @Mock
    InvestmentRoundRepository roundRepo;

    @InjectMocks
    InvestorMockAiRatingService ratingService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    /*
     * // Uncomment this test to run the scheduled logic once
     * 
     * @Autowired
     * private InvestorMockAiRatingService ratingService;
     * 
     * @Autowired
     * private InvestorRepository investorRepo;
     * 
     * 
     * @Test
     * void runInvestorRatingOnce() {
     * // Execute your scheduled logic on demand
     * ratingService.rateAllInvestors();
     * 
     * // Fetch all investors and print their updated ratings
     * System.out.println("---- Investor Ratings After One-Shot Run ----");
     * for (Investor inv : investorRepo.findAll()) {
     * System.out.printf(
     * "%-20s → %s%n",
     * inv.getName(),
     * inv.getAiRating());
     * }
     * }
     */

    @Test
    void rateAllInvestors_computesExpectedMockRating() {
        // 1) Prepare an investor
        Investor inv = new Investor();
        inv.setId("i1");
        inv.setName("Test Investor");
        inv.setEmail("test@example.com");
        assertEquals("3.0", inv.getAiRating());

        // 2) Prepare two transactions both dated today
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        InvestmentTransaction tx1 = new InvestmentTransaction();
        tx1.setId("tx1");
        tx1.setInvestorId("i1");
        tx1.setInvestmentRoundId("r1");
        tx1.setDate(today);
        tx1.setAmount(100_000.0);
        InvestmentTransaction tx2 = new InvestmentTransaction();
        tx2.setId("tx2");
        tx2.setInvestorId("i1");
        tx2.setInvestmentRoundId("r2");
        tx2.setDate(today);
        tx2.setAmount(200_000.0);

        List<InvestmentTransaction> txs = List.of(tx1, tx2);

        when(investorRepo.findAll()).thenReturn(List.of(inv));
        when(txRepo.findByInvestorId("i1")).thenReturn(txs);

        // 3) Stub the rounds: one CLOSED, one EXPIRED
        InvestmentRound r1 = new InvestmentRound();
        r1.setId("r1");
        r1.setStatus(InvestmentStatus.CLOSED);
        InvestmentRound r2 = new InvestmentRound();
        r2.setId("r2");
        r2.setStatus(InvestmentStatus.EXPIRED);

        when(roundRepo.findById("r1")).thenReturn(java.util.Optional.of(r1));
        when(roundRepo.findById("r2")).thenReturn(java.util.Optional.of(r2));

        // 4) Run the rating logic
        ratingService.rateAllInvestors();

        // 5) Compute expected rating manually:
        // count = 2 → normA = 2/50 = 0.04
        // volume = 300k → normV = 300_000/1_000_000 = 0.3
        // success = 1/2 = 0.5
        // recency = 1.0 (both tx are today)
        double engagement = 0.3 * 0.04 + 0.3 * 0.3 + 0.3 * 0.5 + 0.1 * 1.0;
        double raw = 3.0 + engagement * 2.0; // SCALE=2.0
        String expected = BigDecimal.valueOf(raw)
                .setScale(1, RoundingMode.HALF_UP)
                .toString();

        // 6) Assert the investor's aiRating was updated correctly
        assertEquals(expected, inv.getAiRating());

        // 7) Verify saveAll was called with our updated investor
        verify(investorRepo).saveAll(argThat(iterable -> {
            java.util.List<Investor> list = new java.util.ArrayList<>();
            iterable.forEach(list::add);
            return list.size() == 1 && expected.equals(list.get(0).getAiRating());
        }));
    }

    @Test
    void rateAllInvestors_calculatesExpectedMockRating() {
        // --- setup investor ---
        Investor inv = new Investor();
        inv.setId("i1");
        inv.setName("Test Investor");
        inv.setEmail("test@investor.com");
        assertEquals("3.0", inv.getAiRating());

        // --- setup transactions ---
        var tx1 = new InvestmentTransaction();
        tx1.setId("tx1");
        tx1.setInvestorId("i1");
        tx1.setInvestmentRoundId("r1");
        tx1.setDate("2025-05-10");
        tx1.setAmount(100_000.0);

        var tx2 = new InvestmentTransaction();
        tx2.setId("tx2");
        tx2.setInvestorId("i1");
        tx2.setInvestmentRoundId("r2");
        tx2.setDate("2025-04-01");
        tx2.setAmount(200_000.0);

        var tx3 = new InvestmentTransaction();
        tx3.setId("tx3");
        tx3.setInvestorId("i1");
        tx3.setInvestmentRoundId("r3");
        tx3.setDate("2025-03-01");
        tx3.setAmount(300_000.0);

        var tx4 = new InvestmentTransaction();
        tx4.setId("tx4");
        tx4.setInvestorId("i1");
        tx4.setInvestmentRoundId("r4");
        tx4.setDate("2025-02-01");
        tx4.setAmount(400_000.0);

        List<InvestmentTransaction> txs = List.of(tx1, tx2, tx3, tx4);

        when(investorRepo.findAll()).thenReturn(List.of(inv));
        when(txRepo.findByInvestorId("i1")).thenReturn(txs);

        InvestmentRound r1 = new InvestmentRound();
        r1.setId("r1");
        r1.setStatus(InvestmentStatus.CLOSED);
        InvestmentRound r2 = new InvestmentRound();
        r2.setId("r2");
        r2.setStatus(InvestmentStatus.CLOSED);
        InvestmentRound r3 = new InvestmentRound();
        r3.setId("r3");
        r3.setStatus(InvestmentStatus.EXPIRED);
        InvestmentRound r4 = new InvestmentRound();
        r4.setId("r4");
        r4.setStatus(InvestmentStatus.EXPIRED);

        when(roundRepo.findById("r1")).thenReturn(java.util.Optional.of(r1));
        when(roundRepo.findById("r2")).thenReturn(java.util.Optional.of(r2));
        when(roundRepo.findById("r3")).thenReturn(java.util.Optional.of(r3));
        when(roundRepo.findById("r4")).thenReturn(java.util.Optional.of(r4));

        ratingService.rateAllInvestors();

        // manually compute expected rating:
        // count=4 → normA=4/50=0.08
        // totalVol=1e6 → normV=1.0
        // success=2 → normS=2/4=0.5
        // lastDate=2025-05-10 → daysSince≈2 → normR≈1 - 2/90 ≈ 0.978
        // engagement ≈0.3*(0.08+1.0+0.5) + 0.1*0.978 =0.3*1.58+0.0978≈0.5718
        // rating=3.0 + 0.5718*2 = 4.1436 → 4.1
        String expected = "4.1";

        assertEquals(expected, inv.getAiRating());

        // verify we saved back
        verify(investorRepo).saveAll(argThat(iterable -> {
            java.util.List<Investor> list = new java.util.ArrayList<>();
            iterable.forEach(list::add);
            return list.size() == 1 && expected.equals(list.get(0).getAiRating());
        }));
    }
}
