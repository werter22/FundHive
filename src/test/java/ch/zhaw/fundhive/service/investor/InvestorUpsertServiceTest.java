package ch.zhaw.fundhive.service.investor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.oauth2.jwt.Jwt;

import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.repository.InvestorRepository;

public class InvestorUpsertServiceTest {

    @Mock
    private InvestorRepository investorRepository;

    @InjectMocks
    private InvestorUpsertService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void upsertInvestorFromJwt_updatesExistingInvestor() {
        // Arrange
        String id = "INV1";
        String email = "updated@example.com";
        String name = "John Doe";

        Investor existing = new Investor();
        existing.setId(id);
        existing.setName("Old Name");
        existing.setEmail("old@example.com");

        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(id);
        when(jwt.getClaimAsString("email")).thenReturn(email);
        when(jwt.getClaimAsString("given_name")).thenReturn("John");
        when(jwt.getClaimAsString("family_name")).thenReturn("Doe");

        when(investorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(investorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Investor result = service.upsertInvestorFromJwt(jwt);

        // Assert
        assertEquals(email, result.getEmail());
        assertEquals(name, result.getName());
        verify(investorRepository).save(existing);
    }

    @Test
    void upsertInvestorFromJwt_createsNewInvestor() {
        // Arrange
        String id = "INV2";
        String email = "new@example.com";
        String name = "Jane Doe";

        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(id);
        when(jwt.getClaimAsString("email")).thenReturn(email);
        when(jwt.getClaimAsString("given_name")).thenReturn("Jane");
        when(jwt.getClaimAsString("family_name")).thenReturn("Doe");

        when(investorRepository.findById(id)).thenReturn(Optional.empty());
        when(investorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Investor result = service.upsertInvestorFromJwt(jwt);

        // Assert
        assertEquals(id, result.getId());
        assertEquals(email, result.getEmail());
        assertEquals(name, result.getName());
        verify(investorRepository).save(any());
    }

    @Test
    void upsertInvestorFromJwt_fallsBackToSingleNameIfNoGivenFamily() {
        // Arrange
        String id = "INV3";
        String email = "fallback@example.com";
        String fullName = "Fallback Name";

        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(id);
        when(jwt.getClaimAsString("email")).thenReturn(email);
        when(jwt.getClaimAsString("given_name")).thenReturn(null);
        when(jwt.getClaimAsString("family_name")).thenReturn(null);
        when(jwt.getClaimAsString("name")).thenReturn(fullName);

        when(investorRepository.findById(id)).thenReturn(Optional.empty());
        when(investorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Investor result = service.upsertInvestorFromJwt(jwt);

        // Assert
        assertEquals(fullName, result.getName());
    }
}
