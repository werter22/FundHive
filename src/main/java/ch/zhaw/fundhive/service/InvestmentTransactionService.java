package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.dto.InvestmentAllTransactionsDTO;
import ch.zhaw.fundhive.model.dto.InvestmentSummaryDTO;
import ch.zhaw.fundhive.model.dto.InvestorPortfolioDTO;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestmentTransactionService {

        private final InvestmentTransactionRepository repository;
        private final InvestmentRoundRepository investmentRoundRepository;
        private final InvestmentAggregationService aggregationService;

        public List<InvestmentTransaction> getAll() {
                return repository.findAll();
        }

        public InvestmentTransaction create(InvestmentTransaction transaction) {
                InvestmentTransaction savedTransaction = repository.save(transaction);

                // Update the amount_raised in the corresponding investment round
                aggregationService.recalculateAmountRaised(transaction.getInvestmentRoundId());

                return savedTransaction;
        }

        public List<InvestmentTransaction> getFilteredTransactions(
                        String investmentRoundId,
                        String investorId,
                        Double minAmount,
                        Double maxAmount,
                        LocalDate startDate,
                        LocalDate endDate) {
                return repository.filterInvestmentTransactions(
                                investmentRoundId, investorId, minAmount, maxAmount, startDate, endDate);
        }

        public List<InvestmentAllTransactionsDTO> getAllTransactionsForAdmin() {
                return repository.findAll().stream()
                                .sorted(Comparator.comparing(InvestmentTransaction::getDate)) // Assumes date is ISO
                                                                                              // string
                                .map(tx -> new InvestmentAllTransactionsDTO(
                                                tx.getId(),
                                                tx.getInvestorId(),
                                                tx.getInvestmentRoundId(),
                                                resolveStartupId(tx.getInvestmentRoundId()), // helper method below
                                                tx.getAmount(),
                                                tx.getDate()))
                                .collect(Collectors.toList());
        }

        private String resolveStartupId(String investmentRoundId) {
                return investmentRoundRepository.findById(investmentRoundId)
                                .map(investmentRound -> investmentRound.getStartupId())
                                .orElse("unknown-startup"); // fallback if the round doesn't exist
        }

        public InvestorPortfolioDTO getInvestorPortfolio(String investorId) {
                List<InvestmentTransaction> transactions = repository.findByInvestorId(investorId);

                double totalAmount = transactions.stream()
                                .mapToDouble(tx -> tx.getAmount())
                                .sum();

                List<InvestmentAllTransactionsDTO> dtoList = transactions.stream()
                                .map(tx -> new InvestmentAllTransactionsDTO(
                                                tx.getId(),
                                                tx.getInvestorId(),
                                                tx.getInvestmentRoundId(),
                                                resolveStartupId(tx.getInvestmentRoundId()),
                                                tx.getAmount(),
                                                tx.getDate()))
                                .toList();

                return new InvestorPortfolioDTO(
                                new InvestmentSummaryDTO(totalAmount, transactions.size()),
                                dtoList);
        }

}
