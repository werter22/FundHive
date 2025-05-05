package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.service.InvestmentRoundService;
import ch.zhaw.fundhive.service.OwnershipService;
import ch.zhaw.fundhive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InvestmentRoundController {

    @Autowired
    private InvestmentRoundService service;

    @Autowired
    private UserService userService;

    @Autowired
    private OwnershipService ownerService;

    /* --- CRUD Endpoints --- */

    @PostMapping("/investment-rounds")
    public ResponseEntity<InvestmentRound> create(@RequestBody InvestmentRound round) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String me = userService.getCurrentUserId();
        if (!ownerService.ownsStartup(round.getStartupId(), me)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(201).body(service.create(round));
    }

    @PutMapping("/investment-rounds/{id}")
    public ResponseEntity<InvestmentRound> update(@PathVariable String id, @RequestBody InvestmentRound round) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String me = userService.getCurrentUserId();
        if (!ownerService.ownsRound(id, me)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(service.update(id, round));
    }

    /* --- Frontend state Endpoints for Entrepreneur --- */

    @PutMapping("/investment-rounds/{id}/cancel")
    public ResponseEntity<Void> cancelRound(@PathVariable String id) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String me = userService.getCurrentUserId();
        if (!ownerService.ownsRound(id, me)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        service.cancelRound(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/investment-rounds/{id}/open")
    public ResponseEntity<Void> openRound(@PathVariable String id) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String me = userService.getCurrentUserId();
        if (!ownerService.ownsRound(id, me)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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

        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<InvestmentRound> results = service.getAllInvestmentRounds(
                minAmountRaised, maxAmountRaised, startDate, endDate, status);

        return ResponseEntity.ok(results);
    }
}
