package ch.zhaw.fundhive.repository;

import ch.zhaw.fundhive.model.Startup;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StartupRepository extends MongoRepository<Startup, String> {

}
