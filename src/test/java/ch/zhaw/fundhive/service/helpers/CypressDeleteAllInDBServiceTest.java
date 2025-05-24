package ch.zhaw.fundhive.service.helpers;

import ch.zhaw.fundhive.repository.InvestmentRoundRepository;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import ch.zhaw.fundhive.repository.InvestorRepository;
import ch.zhaw.fundhive.repository.StartupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CypressDeleteAllInDBServiceTest {

    @Mock
    private StartupRepository startupRepo;

    @Mock
    private InvestorRepository investorRepo;

    @Mock
    private InvestmentRoundRepository roundRepo;

    @Mock
    private InvestmentTransactionRepository transactionRepo;

    @InjectMocks
    private CypressDeleteAllInDBService service;

    @Test
    void deleteAll_shouldCallDeleteOnAllRepositories() {
        // When
        service.deleteAll();

        // Then: each repo.deleteAll() is invoked exactly once
        verify(startupRepo).deleteAll();
        verify(investorRepo).deleteAll();
        verify(roundRepo).deleteAll();
        verify(transactionRepo).deleteAll();
    }
}
