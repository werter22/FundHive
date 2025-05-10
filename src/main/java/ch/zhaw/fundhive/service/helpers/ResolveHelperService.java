package ch.zhaw.fundhive.service.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.fundhive.model.InvestmentRound;
import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.model.Startup;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.InvestorRepository;
import ch.zhaw.fundhive.repository.StartupRepository;

@Service
public class ResolveHelperService {

    @Autowired
    private InvestmentRoundRepository investmentRoundRepository;

    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private StartupRepository startupRepository;

    /* --- Helper Methods --- */

    public String resolveStartupId(String investmentRoundId) {
        return investmentRoundRepository.findById(investmentRoundId)
                .map(InvestmentRound::getStartupId)
                .orElse("unknown-startup");
    }

    public String resolveRoundName(String investmentRoundId) {
        return investmentRoundRepository.findById(investmentRoundId)
                .map(InvestmentRound::getRound_name)
                .orElse("unknown-round");
    }

    public String resolveInvestorName(String investorId) {
        return investorRepository.findById(investorId)
                .map(Investor::getName)
                .orElse("unknown-investor");
    }

    public String resolveStartupName(String startupId) {
        return startupRepository.findById(startupId)
                .map(Startup::getName)
                .orElse("unknown-startup");
    }

}
