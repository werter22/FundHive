package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.dto.StartupFundingAggregationDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.service.StartupService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/startups")
public class StartupController {
    private final StartupService startupService;

    public StartupController(StartupService startupService) {
        this.startupService = startupService;
    }

    // Get all startups
    @GetMapping
    public List<Startup> getAllStartups() {
        return startupService.getAllStartups();
    }

    // Get a startup by ID
    @GetMapping("/{id}")
    public ResponseEntity<Startup> getStartupById(@PathVariable String id) {
        return startupService.getStartupById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new startup
    @PostMapping
    public ResponseEntity<Startup> createStartup(@RequestBody Startup startup) {
        return ResponseEntity.status(201).body(startupService.createStartup(startup));
    }

    // Update a startup
    @PutMapping("/{id}")
    public ResponseEntity<Startup> updateStartup(@PathVariable String id, @RequestBody Startup startup) {
        return ResponseEntity.ok(startupService.updateStartup(id, startup));
    }

    // Delete a startup
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStartup(@PathVariable String id) {
        startupService.deleteStartup(id);
        return ResponseEntity.noContent().build();
    }

    // Get startups by industry
    @GetMapping("/industry/{industry}")
    public List<Startup> getStartupsByIndustry(@PathVariable IndustryType industry) {
        return startupService.findStartupsByIndustry(industry);
    }

    // Get startups by funding status
    @GetMapping("/funding-status/{status}")
    public List<Startup> getStartupsByFundingStatus(@PathVariable StartupFundingStatus status) {
        return startupService.findStartupsByFundingStatus(status);
    }

    // Get startups within a valuation range
    @GetMapping("/valuation")
    public List<Startup> getStartupsByValuationRange(
            @RequestParam double min,
            @RequestParam double max) {
        return startupService.findStartupsByValuationRange(min, max);
    }

    // Get startups by industry & valuation range
    @GetMapping("/industry/{industry}/valuation")
    public List<Startup> getStartupsByIndustryAndValuation(
            @PathVariable IndustryType industry,
            @RequestParam double min,
            @RequestParam double max) {
        return startupService.findStartupsByIndustryAndValuation(industry, min, max);
    }

    // Get funding status aggregation
    @GetMapping("/aggregation/funding-status")
    public List<StartupFundingAggregationDTO> getFundingStatusAggregation() {
        return startupService.getFundingStatusAggregation();
    }

    @GetMapping("/startups")
    public ResponseEntity<List<Startup>> getFilteredStartups(
            @RequestParam(required = false) IndustryType industry,
            @RequestParam(required = false) StartupFundingStatus fundingStatus,
            @RequestParam(required = false) Double minValuation,
            @RequestParam(required = false) Double maxValuation) {

        List<Startup> startups = startupService.getFilteredStartups(industry, fundingStatus, minValuation,
                maxValuation);
        return new ResponseEntity<>(startups, HttpStatus.OK);
    }

}
