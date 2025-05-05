package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
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

    @Autowired
    private InvestmentRoundRepository investmentRoundRepository;

    public Optional<Startup> getStartupById(String id) {
        return startupRepository.findById(id);
    }

    public Startup createStartup(Startup startup) {
        return startupRepository.save(startup);
    }

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

    public List<Startup> filterStartups(
            IndustryType industry,
            StartupFundingStatus fundingStatus,
            Double minValuation,
            Double maxValuation,
            String name,
            Double aiRating) {
        return startupRepository.findAll().stream()
                .filter(s -> industry == null || s.getIndustry() == industry)
                .filter(s -> fundingStatus == null || s.getFundingStatus() == fundingStatus)
                .filter(s -> minValuation == null || Double.parseDouble(s.getValuation()) >= minValuation)
                .filter(s -> maxValuation == null || Double.parseDouble(s.getValuation()) <= maxValuation)
                .filter(s -> name == null || s.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(s -> aiRating == null || Double.parseDouble(s.getAiRating()) >= aiRating)
                .toList();
    }

    public FundingOverviewDTO getFundingOverview(String startupId) {
        return investmentRoundRepository.getFundingOverview(startupId);
    }

}
