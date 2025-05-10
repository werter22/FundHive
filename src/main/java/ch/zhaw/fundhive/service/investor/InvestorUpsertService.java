package ch.zhaw.fundhive.service.investor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.repository.InvestorRepository;

@Service
public class InvestorUpsertService {

    @Autowired
    private InvestorRepository investorRepository;

    // adds a new investor to the DB on frontend login
    public Investor upsertInvestorFromJwt(Jwt jwt) {
        String id = jwt.getSubject();
        String email = jwt.getClaimAsString("email");

        String given = jwt.getClaimAsString("given_name");
        String family = jwt.getClaimAsString("family_name");
        String fullName = (given != null && family != null)
                ? given + " " + family
                : jwt.getClaimAsString("name");

        return investorRepository.findById(id)
                .map(existing -> {
                    // update any fields you care about
                    existing.setEmail(email);
                    existing.setName(fullName);
                    return investorRepository.save(existing);
                })
                .orElseGet(() -> {
                    // doesn't exist yet → create new
                    Investor inv = new Investor();
                    inv.setId(id);
                    inv.setEmail(email);
                    inv.setName(fullName);
                    return investorRepository.save(inv);
                });
    }
}
