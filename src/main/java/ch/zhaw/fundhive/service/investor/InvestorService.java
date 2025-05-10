package ch.zhaw.fundhive.service.investor;

import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.repository.InvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvestorService {

    @Autowired
    private InvestorRepository investorRepository;

    /* --- CRUD methods --- */

    // return all investors in DB
    public List<Investor> getAllInvestors() {
        return investorRepository.findAll();
    }

    // Creates a new investor in the DB (JSON Body)
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
