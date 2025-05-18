package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.dto.InvestmentRoundCreateDTO;
import ch.zhaw.fundhive.model.enums.InvestmentStatus;
import ch.zhaw.fundhive.service.StartupService;
import ch.zhaw.fundhive.service.helpers.OwnershipService;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.investmentRound.InvestmentRoundService;

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
    private InvestmentRoundService roundService;

    @Autowired
    private StartupService startupService;

    @Autowired
    private UserService userService;

    @Autowired
    private OwnershipService ownerService;

    @PostMapping("/investment-rounds")
    public ResponseEntity<InvestmentRound> create(@RequestBody InvestmentRoundCreateDTO dto) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String userId = userService.getCurrentUserId();
        if (!ownerService.ownsStartup(dto.getStartupId(), userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        InvestmentRound created = roundService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /* --- Get all the rounds for any given startup --- */

    @GetMapping("/investment-rounds/{startupId}")
    public ResponseEntity<List<InvestmentRound>> getInvestmentRoundsForStartup(@PathVariable String startupId) {
        if (!startupService.startupExists(startupId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<InvestmentRound> rounds = roundService.getRoundsByStartupId(startupId);
        return ResponseEntity.ok(rounds);
    }

    /* --- Filter Endpoint for Admin audit --- */

    @GetMapping("/investment-rounds")
    public ResponseEntity<List<InvestmentRound>> getAllInvestmentRounds(
            @RequestParam(required = false) Double minAmountRaised,
            @RequestParam(required = false) Double maxAmountRaised,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateTo,
            @RequestParam(required = false) InvestmentStatus status) {

        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<InvestmentRound> results = roundService.getAllInvestmentRounds(
                minAmountRaised, maxAmountRaised, startDateFrom, startDateTo, endDateFrom, endDateTo, status);

        return ResponseEntity.ok(results);
    }
}
