package com.nineleaps.journalApp.Services;

import com.nineleaps.journalApp.Entities.JournalEntry;


import java.util.List;

public interface EntryService {

    List<JournalEntry> listEntries(String id);

    JournalEntry retrieve(String id);

    JournalEntry create(JournalEntry journalEntry,String id);

    JournalEntry update(String userId, String id, JournalEntry journalEntry);

    void delete(String userId, String id);

    List<JournalEntry> getAllEntries();
}
