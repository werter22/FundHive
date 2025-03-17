package ch.zhaw.fundhive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import ch.zhaw.fundhive.service.InvestorService;
import ch.zhaw.fundhive.model.Investor;

@RestController
@RequestMapping("/api/investors")
public class InvestorController {

    @Autowired
    private InvestorService investorService;

    @GetMapping
    public List<Investor> getAllInvestors() {
        return investorService.getAllInvestors();
    }

    @PostMapping
    public ResponseEntity<Investor> createInvestor(@RequestBody Investor investor) {
        Investor savedInvestor = investorService.createInvestor(investor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInvestor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Investor> updateInvestor(@PathVariable String id, @RequestBody Investor investorDetails) {
        Investor updatedInvestor = investorService.updateInvestor(id, investorDetails);
        return ResponseEntity.ok(updatedInvestor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvestor(@PathVariable String id) {
        investorService.deleteInvestor(id);
        return ResponseEntity.ok("Investor deleted successfully");
    }
}
