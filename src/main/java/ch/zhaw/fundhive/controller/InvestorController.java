package ch.zhaw.fundhive.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.investor.InvestorService;
import ch.zhaw.fundhive.service.investor.InvestorUpsertService;
import ch.zhaw.fundhive.model.Investor;

@RestController
@RequestMapping("/api")
public class InvestorController {

    @Autowired
    private InvestorService service;

    @Autowired
    private InvestorUpsertService upsertService;

    @Autowired
    private UserService userService;

    /* --- CRUD Endpoints --- */

    @GetMapping("/investors")
    public ResponseEntity<?> getAllInvestors() {
        if (!userService.userHasRole("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Investor> investors = service.getAllInvestors();
        return ResponseEntity.ok(investors);
    }

    @PostMapping("/investors/me")
    public ResponseEntity<Void> upsertCurrentInvestor(@AuthenticationPrincipal Jwt jwt) {
        if (!userService.userHasRole("investor")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        upsertService.upsertInvestorFromJwt(jwt);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/investors")
    public ResponseEntity<Investor> createInvestor(@RequestBody Investor investor) {
        Investor savedInvestor = service.createInvestor(investor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInvestor);
    }

    @PutMapping("/investors/{id}")
    public ResponseEntity<Investor> updateInvestor(@PathVariable String id, @RequestBody Investor investorDetails) {
        Investor updatedInvestor = service.updateInvestor(id, investorDetails);
        return ResponseEntity.ok(updatedInvestor);
    }

}
