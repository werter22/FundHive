package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.dto.InvestmentAllTransactionsDTO;
import ch.zhaw.fundhive.model.dto.InvestorPortfolioDTO;
import ch.zhaw.fundhive.service.InvestmentTransactionService;
import ch.zhaw.fundhive.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InvestmentTransactionController {

    @Autowired
    private InvestmentTransactionService service;

    @Autowired
    private UserService userService;

    /* --- CRUD Endpoints --- */

    @PostMapping("/investment-transactions")
    public ResponseEntity<InvestmentTransaction> create(@RequestBody InvestmentTransaction transaction) {
        if (!userService.userHasRole("investor")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        transaction.setInvestorId(userService.getCurrentUserId());

        InvestmentTransaction saved = service.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /* --- Filter Endpoint for Admin audit --- */

    @GetMapping("/investment-transactions")
    public ResponseEntity<List<InvestmentAllTransactionsDTO>> getFilteredTransactions(
            @RequestParam(required = false) String investorId,
            @RequestParam(required = false) String startupId,
            @RequestParam(required = false) String roundId,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(service.getFilteredTransactionsForAdmin(
                investorId, startupId, roundId, minAmount, maxAmount, startDate, endDate));
    }

    /* --- Investor Portfolio --- */

    @GetMapping("/investment-transactions/{id}/portfolio")
    public ResponseEntity<InvestorPortfolioDTO> getPortfolio(@PathVariable String id) {
        if (!userService.userHasRole("investor")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String me = userService.getCurrentUserId();

        if (!me.equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        InvestorPortfolioDTO portfolio = service.getInvestorPortfolio(me);
        return ResponseEntity.ok(portfolio);
    }

}
