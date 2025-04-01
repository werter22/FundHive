package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.service.InvestmentTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
