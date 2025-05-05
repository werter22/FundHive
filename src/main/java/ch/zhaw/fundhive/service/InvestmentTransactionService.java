package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.dto.InvestmentAllTransactionsDTO;
import ch.zhaw.fundhive.model.dto.InvestmentSummaryDTO;
import ch.zhaw.fundhive.model.dto.InvestorPortfolioDTO;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import ch.zhaw.fundhive.repository.InvestorRepository;
import ch.zhaw.fundhive.repository.StartupRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestmentTransactionService {

        @Autowired
        private InvestmentTransactionRepository investmentTransactionRepository;
        @Autowired
        private InvestmentRoundRepository investmentRoundRepository;
        @Autowired
        private InvestorRepository investorRepository;
        @Autowired
        private StartupRepository startupRepository;

        /*
         * --- Create Transaction ---
         * Can only be created if FundingRoundStatus == OPEN)
         */

        public InvestmentTransaction create(InvestmentTransaction transaction) {
                InvestmentRound round = investmentRoundRepository.findById(transaction.getInvestmentRoundId())
                                .orElseThrow(() -> new RuntimeException("Investment round not found"));

                if (round.getStatus() != InvestmentStatus.OPEN) {
                        throw new RuntimeException("Investments can only be made into OPEN rounds.");
                }
                // set today's date (YYYY-MM-DD) before save
                transaction.setDate(LocalDate.now().toString());
                InvestmentTransaction savedTransaction = investmentTransactionRepository.save(transaction);

                // Recalculate amount_raised of given round
                double updatedTotal = investmentTransactionRepository
                                .findByInvestmentRoundId(transaction.getInvestmentRoundId())
                                .stream()
                                .mapToDouble(InvestmentTransaction::getAmount)
                                .sum();

                round.setAmount_raised(updatedTotal);

                // Auto-close if funding goal of this round is met
                if (round.getStatus() == InvestmentStatus.OPEN && updatedTotal >= round.getGoal_amount()) {
                        round.setStatus(InvestmentStatus.CLOSED);
                }

                investmentRoundRepository.save(round);

                return savedTransaction;
        }

        /* --- Admin View of All Transactions --- */

        public List<InvestmentAllTransactionsDTO> getFilteredTransactionsForAdmin(
                        String investorId,
                        String startupId,
                        String roundId,
                        Double minAmount,
                        Double maxAmount,
                        LocalDate startDate,
                        LocalDate endDate) {
                return investmentTransactionRepository.findAll().stream()
                                .filter(tx -> investorId == null || tx.getInvestorId().equals(investorId))
                                .filter(tx -> roundId == null || tx.getInvestmentRoundId().equals(roundId))
                                .filter(tx -> {
                                        if (startupId == null)
                                                return true;
                                        return investmentRoundRepository.findById(tx.getInvestmentRoundId())
                                                        .map(round -> round.getStartupId().equals(startupId))
                                                        .orElse(false);
                                })
                                .filter(tx -> minAmount == null || tx.getAmount() >= minAmount)
                                .filter(tx -> maxAmount == null || tx.getAmount() <= maxAmount)
                                .filter(tx -> startDate == null || !LocalDate.parse(tx.getDate()).isBefore(startDate))
                                .filter(tx -> endDate == null || !LocalDate.parse(tx.getDate()).isAfter(endDate))
                                .map(tx -> new InvestmentAllTransactionsDTO(
                                                tx.getId(),
                                                tx.getInvestorId(),
                                                resolveInvestorName(tx.getInvestorId()),
                                                tx.getInvestmentRoundId(),
                                                resolveRoundName(tx.getInvestmentRoundId()),
                                                resolveStartupId(tx.getInvestmentRoundId()),
                                                resolveStartupName(resolveStartupId(tx.getInvestmentRoundId())),
                                                tx.getAmount(),
                                                tx.getDate()))
                                .toList();
        }

        /* --- Investor Portfolio View --- */

        public InvestorPortfolioDTO getInvestorPortfolio(String investorId) {
                List<InvestmentTransaction> transactions = investmentTransactionRepository.findByInvestorId(investorId);

                double totalAmount = transactions.stream()
                                .mapToDouble(InvestmentTransaction::getAmount)
                                .sum();

                List<InvestmentAllTransactionsDTO> dtoList = transactions.stream()
                                .map(tx -> {
                                        String roundId = tx.getInvestmentRoundId();
                                        String roundName = resolveRoundName(roundId);
                                        String startupId = resolveStartupId(roundId);
                                        String startupName = resolveStartupName(startupId);
                                        String investorName = resolveInvestorName(investorId);

                                        return new InvestmentAllTransactionsDTO(
                                                        tx.getId(),
                                                        investorId,
                                                        investorName,
                                                        roundId,
                                                        roundName,
                                                        startupId,
                                                        startupName,
                                                        tx.getAmount(),
                                                        tx.getDate());
                                })
                                .toList();

                return new InvestorPortfolioDTO(
                                new InvestmentSummaryDTO(totalAmount, transactions.size()),
                                dtoList);
        }

        /* --- Helper Methods --- */

        private String resolveStartupId(String investmentRoundId) {
                return investmentRoundRepository.findById(investmentRoundId)
                                .map(InvestmentRound::getStartupId)
                                .orElse("unknown-startup");
        }

        private String resolveRoundName(String investmentRoundId) {
                return investmentRoundRepository.findById(investmentRoundId)
                                .map(InvestmentRound::getRound_name)
                                .orElse("unknown-round");
        }

        private String resolveInvestorName(String investorId) {
                return investorRepository.findById(investorId)
                                .map(Investor::getName)
                                .orElse("unknown-investor");
        }

        private String resolveStartupName(String startupId) {
                return startupRepository.findById(startupId)
                                .map(Startup::getName)
                                .orElse("unknown-startup");
        }
}
