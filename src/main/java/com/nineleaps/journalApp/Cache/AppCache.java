package com.nineleaps.journalApp.Cache;

import com.nineleaps.journalApp.Entities.ConfigJournalAppEntity;
import com.nineleaps.journalApp.Repositories.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
public class AppCache {

    private final ConfigJournalAppRepository repository;
    private Map<String, String> cache;

    @Autowired
    public AppCache(ConfigJournalAppRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init(){
        cache = new HashMap<>();
        List<ConfigJournalAppEntity> entityList = repository.findAll();
        entityList.forEach(entity -> cache.put(entity.getKey(), entity.getValue()));
    }
}
