package ch.zhaw.fundhive.tools;

import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.model.enums.IndustryType;
import ch.zhaw.fundhive.model.enums.StartupFundingStatus;
import ch.zhaw.fundhive.repository.StartupRepository;
import ch.zhaw.fundhive.service.StartupService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupTools {

    private final StartupRepository startupRepository;
    private final StartupService startupService;

    public StartupTools(StartupRepository startupRepository, StartupService startupService) {
        this.startupRepository = startupRepository;
        this.startupService = startupService;
    }

    @Tool(description = "List all startups with basic details and their description")
    public List<Startup> getAllStartups() {
        return startupRepository.findAll();
    }

    @Tool(description = "Filter startups by industry, funding status, valuation range, name and AI rating")
    public List<Startup> filterStartups(
            String industry,
            String fundingStatus,
            Double minValuation,
            Double maxValuation,
            String name,
            Double aiRating) {
        IndustryType ind = industry != null ? IndustryType.valueOf(industry) : null;
        StartupFundingStatus fs = fundingStatus != null
                ? StartupFundingStatus.valueOf(fundingStatus)
                : null;
        return startupService.filterStartups(
                ind, fs, minValuation, maxValuation, name, aiRating);
    }
}