package com.nineleaps.journalApp.Repositories;

import com.nineleaps.journalApp.Entities.JournalEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends MongoRepository<JournalEntry, String> {

    boolean existsByTitle(String title);
}
