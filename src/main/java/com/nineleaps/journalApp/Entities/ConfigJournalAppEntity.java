package com.nineleaps.journalApp.Entities;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "config_journal_app")
public class ConfigJournalAppEntity {

    private String key;
    private String value;
}
