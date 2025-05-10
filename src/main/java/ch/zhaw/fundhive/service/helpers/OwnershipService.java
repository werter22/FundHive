package ch.zhaw.fundhive.service.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.StartupRepository;

@Service
public class OwnershipService {

    @Autowired
    private StartupRepository startupRepo;

    @Autowired
    private InvestmentRoundRepository repository;

    public boolean ownsStartup(String startupId, String userId) {
        return startupRepo.findById(startupId)
                .map(startup -> startup.getOwnerId().equals(userId))
                .orElse(false);
    }

    public boolean ownsRound(String roundId, String userId) {
        return repository.findById(roundId)
                .flatMap(round -> startupRepo.findById(round.getStartupId()))
                .map(startup -> userId.equals(startup.getOwnerId()))
                .orElse(false);
    }

}
