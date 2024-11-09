package com.nineleaps.journalApp.Repositories;

import com.nineleaps.journalApp.Entities.ConfigJournalAppEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity,String> {
}
