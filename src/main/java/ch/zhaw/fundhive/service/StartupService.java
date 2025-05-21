package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.dto.FundingOverviewDTO;
import ch.zhaw.fundhive.model.dto.StartupCreateDTO;
import ch.zhaw.fundhive.model.dto.StartupUpdateDTO;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.StartupRepository;
import ch.zhaw.fundhive.service.ai.StartupAiRatingService;
import ch.zhaw.fundhive.service.helpers.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StartupService {

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private InvestmentRoundRepository investmentRoundRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StartupAiRatingService AiRatingService;

    public Startup create(StartupCreateDTO dto) {

        Startup startup = new Startup();

        startup.setName(dto.getName());
        startup.setDescription(dto.getDescription());
        startup.setIndustry(dto.getIndustry());
        startup.setValuation(dto.getValuation());
        startup.setFundingStatus(dto.getFundingStatus());
        startup.setOwnerId(userService.getCurrentUserId());
        try {
            startup.setAiRating(AiRatingService.rateStartupOnCreation(startup));
        } catch (Exception ignored) {
            // AI rating failed, default "0.0" will be used
        }
        return startupRepository.save(startup);
    }

    public Optional<Startup> getStartupById(String starupId) {
        return startupRepository.findById(starupId);
    }

    public Startup updateStartup(String startupId, StartupUpdateDTO startup) {
        Optional<Startup> startupOptional = startupRepository.findById(startupId);

        Startup existingStartup = startupOptional.get();
        existingStartup.setName(startup.getName());
        existingStartup.setDescription(startup.getDescription());
        existingStartup.setIndustry(startup.getIndustry());
        existingStartup.setValuation(startup.getValuation());
        existingStartup.setFundingStatus(startup.getFundingStatus());

        return startupRepository.save(existingStartup);
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
                .filter(s -> minValuation == null || s.getValuation() >= minValuation)
                .filter(s -> maxValuation == null || s.getValuation() <= maxValuation)
                .filter(s -> name == null || s.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(s -> aiRating == null || Double.parseDouble(s.getAiRating()) >= aiRating)
                .toList();
    }

    public FundingOverviewDTO getFundingOverview(String startupId) {
        return investmentRoundRepository.getFundingOverview(startupId);
    }

    public boolean startupExists(String startupId) {
        return startupRepository.existsById(startupId);
    }
}
