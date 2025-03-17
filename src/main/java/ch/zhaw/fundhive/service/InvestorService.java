package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.Investor;
import ch.zhaw.fundhive.repository.InvestorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvestorService {

    @Autowired
    private InvestorRepository investorRepository;

    public List<Investor> getAllInvestors() {
        return investorRepository.findAll();
    }

    public Investor createInvestor(Investor investor) {
        return investorRepository.save(investor);
    }

    public Investor updateInvestor(String id, Investor investorDetails) {
        return investorRepository.findById(id).map(existingInvestor -> {
            existingInvestor.setName(investorDetails.getName());
            existingInvestor.setEmail(investorDetails.getEmail());
            existingInvestor.setAiRating(investorDetails.getAiRating());
            return investorRepository.save(existingInvestor);
        }).orElseThrow(() -> new RuntimeException("Investor not found"));
    }

    public void deleteInvestor(String id) {
        investorRepository.deleteById(id);
    }
}
