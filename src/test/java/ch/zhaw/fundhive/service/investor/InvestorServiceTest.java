package ch.zhaw.fundhive.service.investor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.repository.InvestorRepository;

public class InvestorServiceTest {

    @Mock
    private InvestorRepository investorRepository;

    @InjectMocks
    private InvestorService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllInvestors_returnsList() {
        List<Investor> mockInvestors = List.of(new Investor(), new Investor());
        when(investorRepository.findAll()).thenReturn(mockInvestors);

        List<Investor> result = service.getAllInvestors();

        assertEquals(2, result.size());
        verify(investorRepository).findAll();
    }

    @Test
    void createInvestor_savesAndReturnsInvestor() {
        Investor newInvestor = new Investor();
        when(investorRepository.save(newInvestor)).thenReturn(newInvestor);

        Investor result = service.createInvestor(newInvestor);

        assertEquals(newInvestor, result);
        verify(investorRepository).save(newInvestor);
    }

    @Test
    void updateInvestor_updatesExistingInvestor() {
        Investor existing = new Investor();
        existing.setId("INV1");
        Investor updates = new Investor();
        updates.setName("Updated Name");
        updates.setEmail("updated@example.com");
        updates.setAiRating("5");

        when(investorRepository.findById("INV1")).thenReturn(Optional.of(existing));
        when(investorRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Investor result = service.updateInvestor("INV1", updates);

        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("5", result.getAiRating());
        verify(investorRepository).save(existing);
    }

    @Test
    void updateInvestor_throwsIfNotFound() {
        when(investorRepository.findById("INV1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.updateInvestor("INV1", new Investor()));

        assertEquals("Investor not found", exception.getMessage());
    }
}
