package ch.zhaw.fundhive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.fundhive.model.dto.InvestorPortfolioDTO;
import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.investor.InvestorPortfolioViewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InvestorPortfolioViewController {

    @Autowired
    private InvestorPortfolioViewService service;

    @Autowired
    private UserService userService;

    @GetMapping("/investment-transactions/{id}/portfolio")
    public ResponseEntity<InvestorPortfolioDTO> getPortfolio(@PathVariable String id) {
        if (!userService.userHasRole("investor")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String userId = userService.getCurrentUserId();
        if (!id.equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        InvestorPortfolioDTO portfolio = service.getInvestorPortfolio(id);
        return ResponseEntity.ok(portfolio);
    }

}
