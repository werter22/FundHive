package ch.zhaw.fundhive.service.helpers;

import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import ch.zhaw.fundhive.repository.InvestorRepository;
import ch.zhaw.fundhive.repository.StartupRepository;
import ch.zhaw.fundhive.repository.InvestmentRoundRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CypressDeleteAllInDBService {

    @Autowired
    private StartupRepository startupRepo;

    @Autowired
    private InvestmentRoundRepository roundRepo;

    @Autowired
    private InvestmentTransactionRepository transactionRepo;

    @Autowired
    private InvestorRepository investorRepo;

    public void deleteAll() {
        startupRepo.deleteAll();
        investorRepo.deleteAll();
        roundRepo.deleteAll();
        transactionRepo.deleteAll();
    }

}
