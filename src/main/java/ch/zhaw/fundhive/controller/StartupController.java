package ch.zhaw.fundhive.controller;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;
import ch.zhaw.fundhive.model.dto.StartupCreateDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.service.StartupService;
import ch.zhaw.fundhive.service.helpers.OwnershipService;
import ch.zhaw.fundhive.service.helpers.UserService;

import java.util.Optional;
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
    public ResponseEntity<Startup> createStartup(@RequestBody StartupCreateDTO sDTO) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Startup created = service.create(sDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/startups/{id}")
    public ResponseEntity<Startup> getStartupById(@PathVariable String id) {
        Optional<Startup> startup = service.getStartupById(id);
        if (startup.isPresent()) {
            return new ResponseEntity<>(startup.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/startups/{id}")
    public ResponseEntity<Startup> updateStartup(@PathVariable String id, @RequestBody Startup startup) {
        if (!userService.userHasRole("entrepreneur")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String userId = userService.getCurrentUserId();
        if (!ownerService.ownsStartup(startup.getOwnerId(), userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(overview);
    }

}
