package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.service.InvestmentRoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/investment-rounds")
public class InvestmentRoundController {

    @Autowired
    private InvestmentRoundService service;

    @GetMapping
    public List<InvestmentRound> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<InvestmentRound> create(@RequestBody InvestmentRound round) {
        return ResponseEntity.status(201).body(service.create(round));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvestmentRound> update(@PathVariable String id, @RequestBody InvestmentRound round) {
        return ResponseEntity.ok(service.update(id, round));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/check-close")
    public ResponseEntity<Void> checkAndClose(@PathVariable String id) {
        service.checkAndClose(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRound(@PathVariable String id) {
        service.cancelRound(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/open")
    public ResponseEntity<Void> openRound(@PathVariable String id) {
        service.openRound(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<InvestmentRound>> getFilteredInvestmentRounds(
            @RequestParam double minAmountRaised,
            @RequestParam double maxAmountRaised,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<InvestmentRound> results = service.getFilteredInvestmentRounds(
                minAmountRaised, maxAmountRaised, startDate, endDate);
        return ResponseEntity.ok(results);
    }
}
