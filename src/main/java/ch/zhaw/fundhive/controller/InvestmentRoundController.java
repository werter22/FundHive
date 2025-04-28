package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.service.InvestmentRoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InvestmentRoundController {

    @Autowired
    private InvestmentRoundService service;

    /* --- CRUD Endpoints --- */

    @PostMapping("/investment-rounds")
    public ResponseEntity<InvestmentRound> create(@RequestBody InvestmentRound round) {
        return ResponseEntity.status(201).body(service.create(round));
    }

    @PutMapping("/investment-rounds/{id}")
    public ResponseEntity<InvestmentRound> update(@PathVariable String id, @RequestBody InvestmentRound round) {
        return ResponseEntity.ok(service.update(id, round));
    }

    /* --- Frontend state Endpoints for Entrepreneur --- */

    @PutMapping("/investment-rounds/{id}/cancel")
    public ResponseEntity<Void> cancelRound(@PathVariable String id) {
        service.cancelRound(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/investment-rounds/{id}/open")
    public ResponseEntity<Void> openRound(@PathVariable String id) {
        service.openRound(id);
        return ResponseEntity.ok().build();
    }

    /* --- Get all the rounds for any given startup --- */

    @GetMapping("/investment-rounds/{startupId}")
    public ResponseEntity<List<InvestmentRound>> getInvestmentRoundsForStartup(@PathVariable String startupId) {
        List<InvestmentRound> rounds = service.getRoundsByStartupId(startupId);
        return ResponseEntity.ok(rounds);
    }

    /* --- Filter Endpoint for Admin audit --- */

    @GetMapping("/investment-rounds")
    public ResponseEntity<List<InvestmentRound>> getAllInvestmentRounds(
            @RequestParam(required = false) Double minAmountRaised,
            @RequestParam(required = false) Double maxAmountRaised,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) InvestmentStatus status) {

        List<InvestmentRound> results = service.getAllInvestmentRounds(
                minAmountRaised, maxAmountRaised, startDate, endDate, status);

        return ResponseEntity.ok(results);
    }
}
