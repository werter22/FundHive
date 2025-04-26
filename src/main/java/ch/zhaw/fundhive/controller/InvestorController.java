package ch.zhaw.fundhive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import ch.zhaw.fundhive.service.InvestorService;
import ch.zhaw.fundhive.model.Investor;

@RestController
@RequestMapping("/api")
public class InvestorController {

    @Autowired
    private InvestorService service;

    /* --- CRUD Endpoints --- */

    @GetMapping("/investors")
    public List<Investor> getAllInvestors() {
        return service.getAllInvestors();
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
