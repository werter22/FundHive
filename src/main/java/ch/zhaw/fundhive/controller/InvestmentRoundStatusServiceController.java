package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.InvestmentRound;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.fundhive.service.helpers.OwnershipService;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.investmentRound.InvestmentRoundStatusService;

@RestController
@RequestMapping("/api")
public class InvestmentRoundStatusServiceController {

    @Autowired
    private UserService userService;

    @Autowired
    private OwnershipService ownerService;

    @Autowired
    private InvestmentRoundStatusService statusService;

    /* --- Frontend state Endpoints for Entrepreneur --- */

    @PutMapping("/investment-rounds/{id}/cancel")
    public ResponseEntity<InvestmentRound> cancelRound(@PathVariable String id) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String userId = userService.getCurrentUserId();
        if (!ownerService.ownsRound(id, userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<InvestmentRound> round = statusService.cancelRound(id);
        if (round.isPresent()) {
            return new ResponseEntity<>(round.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/investment-rounds/{id}/open")
    public ResponseEntity<InvestmentRound> openRound(@PathVariable String id) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String userId = userService.getCurrentUserId();
        if (!ownerService.ownsRound(id, userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<InvestmentRound> round = statusService.openRound(id);
        if (round.isPresent()) {
            return new ResponseEntity<>(round.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
