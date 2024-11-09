package com.nineleaps.journalApp.Service;

import com.nineleaps.journalApp.Entities.JournalEntry;
import com.nineleaps.journalApp.Entities.User;
import com.nineleaps.journalApp.Exceptions.AlreadyExistsException;
import com.nineleaps.journalApp.Exceptions.ResourceNotFoundException;
import com.nineleaps.journalApp.Repositories.EntryRepository;
import com.nineleaps.journalApp.Services.Impls.EntryServiceImpl;
import com.nineleaps.journalApp.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EntryServiceTests {

    @Mock
    private EntryRepository entryRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    private EntryServiceImpl entryService;

    private User user;
    private JournalEntry entry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("123");
        user.setJournalEntries(new ArrayList<>());

        entry = new JournalEntry();
        entry.setId("456");
        entry.setTitle("Test Title");
        entry.setContent("Test Content");
        entry.setDate(LocalDateTime.now());
    }

    @Test
    void listEntries_Success() {
        user.getJournalEntries().add(entry);
        when(userService.retrieve("123")).thenReturn(user);

        List<JournalEntry> entries = entryService.listEntries("123");

        assertNotNull(entries);
        assertEquals(1, entries.size());
        assertEquals("Test Title", entries.get(0).getTitle());
        verify(userService, times(1)).retrieve("123");
    }

    @Test
    void retrieve_EntryExists() {
        when(entryRepo.findById("456")).thenReturn(Optional.of(entry));

        JournalEntry retrievedEntry = entryService.retrieve("456");

        assertNotNull(retrievedEntry);
        assertEquals("Test Title", retrievedEntry.getTitle());
        verify(entryRepo, times(1)).findById("456");
    }

    @Test
    void retrieve_EntryNotFound() {
        when(entryRepo.findById("456")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> entryService.retrieve("456"));

        verify(entryRepo, times(1)).findById("456");
    }

    @Test
    @Transactional
    void create_Success() {
        when(userService.retrieve("123")).thenReturn(user);
        when(entryRepo.existsByTitle("Test Title")).thenReturn(false);
        when(entryRepo.save(any(JournalEntry.class))).thenReturn(entry);

        JournalEntry createdEntry = entryService.create(entry, "123");

        assertNotNull(createdEntry);
        assertEquals("Test Title", createdEntry.getTitle());
        assertNotNull(createdEntry.getDate());
        assertTrue(user.getJournalEntries().contains(createdEntry));
        verify(userService, times(1)).retrieve("123");
        verify(entryRepo, times(1)).existsByTitle("Test Title");
        verify(entryRepo, times(1)).save(any(JournalEntry.class));
        verify(userService, times(1)).update("123", user);
    }

    @Test
    @Transactional
    void create_EntryAlreadyExists() {
        when(userService.retrieve("123")).thenReturn(user);
        when(entryRepo.existsByTitle("Test Title")).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> entryService.create(entry, "123"));

        verify(entryRepo, times(1)).existsByTitle("Test Title");
    }

    @Test
    void update_EntryExists() {
        when(userService.retrieve("123")).thenReturn(user);
        when(entryRepo.findById("456")).thenReturn(Optional.of(entry));
        when(entryRepo.save(any(JournalEntry.class))).thenReturn(entry);

        JournalEntry newEntry = new JournalEntry();
        newEntry.setTitle("Updated Title");
        newEntry.setContent("Updated Content");

        JournalEntry updatedEntry = entryService.update("123", "456", newEntry);

        assertEquals("Updated Title", updatedEntry.getTitle());
        assertEquals("Updated Content", updatedEntry.getContent());
        assertNotNull(updatedEntry.getDate());
        verify(entryRepo, times(1)).findById("456");
        verify(entryRepo, times(1)).save(any(JournalEntry.class));
    }

    @Test
    void update_EntryNotFound() {
        when(userService.retrieve("123")).thenReturn(user);
        when(entryRepo.findById("456")).thenReturn(Optional.empty());

        JournalEntry newEntry = new JournalEntry();
        newEntry.setTitle("Updated Title");

        assertThrows(ResourceNotFoundException.class, () -> entryService.update("123", "456", newEntry));

        verify(entryRepo, times(1)).findById("456");
    }

    @Test
    @Transactional
    void delete_Success() {
        when(userService.retrieve("123")).thenReturn(user);
        when(entryRepo.findById("456")).thenReturn(Optional.of(entry));

        user.getJournalEntries().add(entry);

        entryService.delete("123", "456");

        assertFalse(user.getJournalEntries().contains(entry));
        verify(userService, times(1)).retrieve("123");
        verify(entryRepo, times(1)).findById("456");
        verify(entryRepo, times(1)).delete(entry);
        verify(userService, times(1)).update("123", user);
    }

    @Test
    void delete_EntryNotFound() {
        when(userService.retrieve("123")).thenReturn(user);
        when(entryRepo.findById("456")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> entryService.delete("123", "456"));

        verify(entryRepo, times(1)).findById("456");
    }

    @Test
    void getAllEntries_Success() {
        when(entryRepo.findAll()).thenReturn(List.of(entry));

        List<JournalEntry> entries = entryService.getAllEntries();

        assertNotNull(entries);
        assertEquals(1, entries.size());
        verify(entryRepo, times(1)).findAll();
    }
}
