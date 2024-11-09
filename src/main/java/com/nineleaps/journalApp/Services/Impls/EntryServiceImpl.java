package com.nineleaps.journalApp.Services.Impls;

import com.nineleaps.journalApp.Entities.JournalEntry;
import com.nineleaps.journalApp.Entities.User;
import com.nineleaps.journalApp.Exceptions.AlreadyExistsException;
import com.nineleaps.journalApp.Exceptions.ResourceNotFoundException;
import com.nineleaps.journalApp.Repositories.EntryRepository;
import com.nineleaps.journalApp.Services.EntryService;
import com.nineleaps.journalApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.nineleaps.journalApp.Utils.Constants.ENTRY;

@Service
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepo;
    private final UserService userService;

    @Autowired
    public EntryServiceImpl(EntryRepository entryRepo, UserService userService) {
        this.entryRepo = entryRepo;
        this.userService = userService;
    }

    @Override
    public List<JournalEntry> listEntries(String id) {
        User user = userService.retrieve(id);
        return user.getJournalEntries();
    }

    @Override
    public JournalEntry retrieve(String id) {
        return entryRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(ENTRY,"Id",id));
    }

    @Override
    @Transactional
    public JournalEntry create(JournalEntry journalEntry, String userId) {
        User user = userService.retrieve(userId);
        if(entryRepo.existsByTitle(journalEntry.getTitle().trim())){
            throw new AlreadyExistsException(ENTRY,"Title", journalEntry.getTitle());
        }
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry savedEntry = entryRepo.save(journalEntry);
        user.getJournalEntries().add(savedEntry);
        userService.update(userId, user);
        return savedEntry;
    }

    @Override
    public JournalEntry update(String userId, String id, JournalEntry newEntry) {
        userService.retrieve(userId);
        JournalEntry oldEntry = entryRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(ENTRY,"Id",id));
        oldEntry.setTitle(newEntry.getTitle());
        oldEntry.setContent(newEntry.getContent());
        oldEntry.setDate(LocalDateTime.now());
        return entryRepo.save(oldEntry);
    }

    @Override
    @Transactional
    public void delete(String userId, String id) {
        User user = userService.retrieve(userId);
        JournalEntry entry = entryRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(ENTRY,"Id",id));
        user.getJournalEntries().removeIf(x->x.getId().equals(id));
        userService.update(userId,user);
        entryRepo.delete(entry);
    }

    @Override
    public List<JournalEntry> getAllEntries(){
        return entryRepo.findAll();
    }
}
