package com.nineleaps.journalApp.Controllers;


import com.nineleaps.journalApp.Dtos.ApiResponse;
import com.nineleaps.journalApp.Entities.JournalEntry;
import com.nineleaps.journalApp.Services.EntryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/journal")
@PreAuthorize("hasAnyRole('ADMIN_USER','NORMAL_USER')")
public class EntryController {

    private final EntryService entryService;


    @Autowired
    public EntryController(EntryService entryService) {
        this.entryService = entryService;

    }

    @GetMapping("/list/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN_READ','USER_READ')")
    public ResponseEntity<List<JournalEntry>> getAllEntriesByUser(@PathVariable String userId){
        return ResponseEntity.ok(entryService.listEntries(userId));
    }

    @PostMapping("/create/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN_CREATE','USER_CREATE')")
    public ResponseEntity<JournalEntry> createEntry(@Valid @RequestBody JournalEntry entry,
                                                    @PathVariable String userId){
        return new ResponseEntity<>(entryService.create(entry,userId), HttpStatus.CREATED);
    }

    @GetMapping("/get/id/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_READ','USER_READ')")
    public ResponseEntity<JournalEntry> readEntry(@PathVariable String id){
        return ResponseEntity.ok(entryService.retrieve(id));
    }

    @PutMapping("/update/user/{userId}/id/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_UPDATE','USER_UPDATE')")
    public ResponseEntity<JournalEntry> updateEntry(@PathVariable String userId, @PathVariable String id,
                                                    @Valid @RequestBody JournalEntry entry){
            return ResponseEntity.ok(entryService.update(userId,id,entry));
    }

    @DeleteMapping("/delete/user/{userId}/entryId/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_DELETE','USER_DELETE')")
    public ResponseEntity<ApiResponse> deleteEntry(@PathVariable String userId,@PathVariable String id){
        entryService.delete(userId,id);
        return new ResponseEntity<>( new ApiResponse("Entry Deleted successfully",true),HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN_READ')")
    public ResponseEntity<List<JournalEntry>> getAllEntries(){
        return ResponseEntity.ok(entryService.getAllEntries());
    }

}
