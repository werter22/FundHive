package ch.zhaw.fundhive.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import ch.zhaw.fundhive.model.Investor;

@Repository
public interface InvestorRepository extends MongoRepository<Investor, String> {
    List<Investor> findByAiRating(String aiRating);
}
