package com.nineleaps.journalApp.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nineleaps.journalApp.Enums.Sentiment;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JournalEntry {
    @Id
    private String id;
    @NotBlank
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    private Sentiment sentiment;
}
