package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.dto.InvestmentAllTransactionsDTO;
import ch.zhaw.fundhive.service.InvestmentTransactionService;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/investment-transactions")
@RequiredArgsConstructor
public class InvestmentTransactionController {

    private final InvestmentTransactionService service;

    @GetMapping
    public List<InvestmentTransaction> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvestmentTransaction create(@RequestBody InvestmentTransaction transaction) {
        return service.create(transaction);
    }

    @GetMapping("/all")
    public ResponseEntity<List<InvestmentAllTransactionsDTO>> getAllTransactionsForAdmin() {
        List<InvestmentAllTransactionsDTO> result = service.getAllTransactionsForAdmin();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/investment-transactions")
    public ResponseEntity<List<InvestmentTransaction>> getFilteredTransactions(
            @RequestParam(required = false) String investmentRoundId,
            @RequestParam(required = false) String investorId,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<InvestmentTransaction> results = service.getFilteredTransactions(
                investmentRoundId, investorId, minAmount, maxAmount, startDate, endDate);
        return ResponseEntity.ok(results);
    }

}
