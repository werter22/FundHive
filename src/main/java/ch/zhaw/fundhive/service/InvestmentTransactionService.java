package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.dto.InvestmentAllTransactionsDTO;
import ch.zhaw.fundhive.model.dto.InvestmentTransactionCreateDTO;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import ch.zhaw.fundhive.service.helpers.ResolveHelperService;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.investmentRound.InvestmentRoundAmountCalcService;
import ch.zhaw.fundhive.service.investmentRound.InvestmentRoundStatusService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvestmentTransactionService {

        @Autowired
        private InvestmentTransactionRepository investmentTransactionRepository;

        @Autowired
        private InvestmentRoundRepository investmentRoundRepository;

        @Autowired
        private ResolveHelperService resolveHelperService;

        @Autowired
        private InvestmentRoundAmountCalcService calculationService;

        @Autowired
        private InvestmentRoundStatusService investmentRoundStatusService;

        @Autowired
        private UserService userService;

        /*
         * --- Create Transaction ---
         * Can only be created if FundingRoundStatus == OPEN)
         */

        public Optional<InvestmentTransaction> create(InvestmentTransactionCreateDTO tDTO) {
                Optional<InvestmentRound> roundOptional = investmentRoundRepository
                                .findById(tDTO.getInvestmentRoundId());
                if (roundOptional.isEmpty() || roundOptional.get().getStatus() != InvestmentStatus.OPEN) {
                        return Optional.empty();
                }

                InvestmentTransaction tx = new InvestmentTransaction();
                tx.setInvestmentRoundId(tDTO.getInvestmentRoundId());
                tx.setInvestorId(userService.getCurrentUserId());
                tx.setAmount(tDTO.getAmount());
                tx.setDate(LocalDate.now().toString());

                InvestmentTransaction created = investmentTransactionRepository.save(tx);

                // Update the amount raised in the invested round
                InvestmentRound round = roundOptional.get();
                round.setAmount_raised(calculationService.getTotalRaised(round.getId()));
                investmentRoundRepository.save(round);

                // Close round if target amount is reached or surpassed
                investmentRoundStatusService.tryCloseIfGoalReached(round);

                return Optional.of(created);
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
                                .map(this::mapToDTO)
                                .toList();
        }

        private InvestmentAllTransactionsDTO mapToDTO(InvestmentTransaction tx) {
                String roundId = tx.getInvestmentRoundId();
                String startupId = resolveHelperService.resolveStartupId(roundId);

                return new InvestmentAllTransactionsDTO(
                                tx.getId(),
                                tx.getInvestorId(),
                                resolveHelperService.resolveInvestorName(tx.getInvestorId()),
                                roundId,
                                resolveHelperService.resolveRoundName(roundId),
                                startupId,
                                resolveHelperService.resolveStartupName(startupId),
                                tx.getAmount(),
                                tx.getDate());
        }

}
