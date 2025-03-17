package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.StartupFundingAggregationDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.repository.StartupRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class StartupService {

    @Autowired
    private StartupRepository startupRepository;

    public StartupService(StartupRepository startupRepository) {
        this.startupRepository = startupRepository;
    }

    // Fetch all startups
    public List<Startup> getAllStartups() {
        return startupRepository.findAll();
    }

    // Fetch a single startup by ID
    public Optional<Startup> getStartupById(String id) {
        return startupRepository.findById(id);
    }

    // Create a new startup
    public Startup createStartup(Startup startup) {
        return startupRepository.save(startup);
    }

    // Update an existing startup
    public Startup updateStartup(String id, Startup updatedStartup) {
        return startupRepository.findById(id)
                .map(existingStartup -> {
                    existingStartup.setName(updatedStartup.getName());
                    existingStartup.setDescription(updatedStartup.getDescription());
                    existingStartup.setIndustry(updatedStartup.getIndustry());
                    existingStartup.setValuation(updatedStartup.getValuation());
                    existingStartup.setFundingStatus(updatedStartup.getFundingStatus());
                    existingStartup.setAiRating(updatedStartup.getAiRating());
                    return startupRepository.save(existingStartup);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Startup not found"));
    }

    // Delete a startup
    public void deleteStartup(String id) {
        if (!startupRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Startup not found");
        }
        startupRepository.deleteById(id);
    }

    // Find startups by industry
    public List<Startup> findStartupsByIndustry(IndustryType industry) {
        return startupRepository.findByIndustry(industry);
    }

    // Find startups by funding status
    public List<Startup> findStartupsByFundingStatus(StartupFundingStatus status) {
        return startupRepository.findByFundingStatus(status);
    }

    // Find startups within a valuation range
    public List<Startup> findStartupsByValuationRange(double min, double max) {
        return startupRepository.findByValuationBetween(min, max);
    }

    // Find startups by industry & valuation range
    public List<Startup> findStartupsByIndustryAndValuation(IndustryType industry, double min, double max) {
        return startupRepository.findByIndustryAndValuationBetween(industry, min, max);
    }

    // Aggregation: Get funding status distribution
    public List<StartupFundingAggregationDTO> getFundingStatusAggregation() {
        return startupRepository.getFundingStatusAggregation();
    }
}
