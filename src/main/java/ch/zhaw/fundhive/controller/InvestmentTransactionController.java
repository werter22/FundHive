package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.dto.InvestmentAllTransactionsDTO;
import ch.zhaw.fundhive.model.dto.InvestorPortfolioDTO;
import ch.zhaw.fundhive.service.InvestmentTransactionService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InvestmentTransactionController {

    @Autowired
    private final InvestmentTransactionService service;

    /* --- CRUD Endpoints --- */

    @PostMapping("/investment-transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public InvestmentTransaction create(@RequestBody InvestmentTransaction transaction) {
        return service.create(transaction);
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
        return ResponseEntity.ok(service.getFilteredTransactionsForAdmin(
                investorId, startupId, roundId, minAmount, maxAmount, startDate, endDate));
    }

    /* --- Investor Portfolio --- */

    @GetMapping("/investment-transactions/{id}/portfolio")
    public ResponseEntity<InvestorPortfolioDTO> getPortfolio(@PathVariable String id) {
        InvestorPortfolioDTO portfolio = service.getInvestorPortfolio(id);
        return ResponseEntity.ok(portfolio);
    }

}
