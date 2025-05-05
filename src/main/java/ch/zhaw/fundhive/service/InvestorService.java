package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.repository.InvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.security.oauth2.jwt.Jwt;

@Service
public class InvestorService {

    @Autowired
    private InvestorRepository investorRepository;

    /* --- CRUD methods --- */

    // return all investors in DB
    public List<Investor> getAllInvestors() {
        return investorRepository.findAll();
    }

    // adds a new investor to the DB
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
                    inv.setAiRating("3.00");
                    return investorRepository.save(inv);
                });
    }

    public Investor createInvestor(Investor investor) {
        return investorRepository.save(investor);
    }

    // updates an existing investor in the DB
    public Investor updateInvestor(String id, Investor investorDetails) {
        return investorRepository.findById(id).map(existingInvestor -> {
            existingInvestor.setName(investorDetails.getName());
            existingInvestor.setEmail(investorDetails.getEmail());
            existingInvestor.setAiRating(investorDetails.getAiRating());
            return investorRepository.save(existingInvestor);
        }).orElseThrow(() -> new RuntimeException("Investor not found"));
    }

}
