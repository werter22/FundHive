package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.model.dto.InvestmentAllTransactionsDTO;
import ch.zhaw.fundhive.model.dto.InvestmentTransactionCreateDTO;
import ch.zhaw.fundhive.service.InvestmentTransactionService;
import ch.zhaw.fundhive.service.helpers.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InvestmentTransactionController {

    @Autowired
    private InvestmentTransactionService transactionService;

    @Autowired
    private UserService userService;

    /* --- CRUD Endpoints --- */

    @PostMapping("/investment-transactions")
    public ResponseEntity<InvestmentTransaction> create(@RequestBody InvestmentTransactionCreateDTO tDTO) {
        if (!userService.userHasRole("investor")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<InvestmentTransaction> transaction = transactionService.create(tDTO);
        if (transaction.isPresent()) {
            return new ResponseEntity<>(transaction.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

        List<InvestmentAllTransactionsDTO> result = transactionService.getFilteredTransactionsForAdmin(
                investorId, startupId, roundId, minAmount, maxAmount, startDate, endDate);

        return ResponseEntity.ok(result);
    }
}
