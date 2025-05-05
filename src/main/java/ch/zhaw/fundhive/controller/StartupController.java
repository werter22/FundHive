package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.service.OwnershipService;
import ch.zhaw.fundhive.service.StartupService;
import ch.zhaw.fundhive.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StartupController {

    @Autowired
    private StartupService service;

    @Autowired
    private UserService userService;

    @Autowired
    private OwnershipService ownerService;

    /* --- CRUD Endpoints --- */

    @PostMapping("/startups")
    public ResponseEntity<Startup> createStartup(@RequestBody Startup startup) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        startup.setOwnerId(userService.getCurrentUserId());
        return ResponseEntity.status(201).body(service.createStartup(startup));
    }

    @GetMapping("/startups/{id}")
    public ResponseEntity<Startup> getStartupById(@PathVariable String id) {
        return service.getStartupById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/startups/{id}")
    public ResponseEntity<Startup> updateStartup(@PathVariable String id, @RequestBody Startup startup) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String me = userService.getCurrentUserId();

        if (!ownerService.ownsStartup(id, me)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(service.updateStartup(id, startup));
    }

    /* --- Filtered Endpoint --- */

    @GetMapping("/startups")
    public ResponseEntity<List<Startup>> getFilteredStartups(
            @RequestParam(required = false) IndustryType industry,
            @RequestParam(required = false) StartupFundingStatus fundingStatus,
            @RequestParam(required = false) Double minValuation,
            @RequestParam(required = false) Double maxValuation,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double aiRating) {
        List<Startup> filtered = service.filterStartups(industry, fundingStatus, minValuation, maxValuation,
                name, aiRating);
        return new ResponseEntity<>(filtered, HttpStatus.OK);
    }

    /* --- Overview for Startups --- */

    @GetMapping("/startups/{id}/funding-overview")
    public ResponseEntity<FundingOverviewDTO> getFundingOverview(@PathVariable("id") String startupId) {
        FundingOverviewDTO overview = service.getFundingOverview(startupId);
        if (overview == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(overview);
    }

}
