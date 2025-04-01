package ch.zhaw.fundhive.service;

import ch.zhaw.fundhive.model.InvestmentTransaction;
import ch.zhaw.fundhive.repository.InvestmentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestmentTransactionService {

    private final InvestmentTransactionRepository repository;

    public List<InvestmentTransaction> getAll() {
        return repository.findAll();
    }

    public InvestmentTransaction create(InvestmentTransaction transaction) {
        return repository.save(transaction);
    }
}
