package com.nineleaps.journalApp.Repositories;

import com.nineleaps.journalApp.Entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByName(String name);

    Optional<User> findByName(String name);

}
