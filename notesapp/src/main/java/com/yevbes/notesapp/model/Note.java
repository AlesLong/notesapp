package com.yevbes.notesapp.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@Document(collection = "notes")
public class Note {
    @Id
    private String id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Text is mandatory")
    private String text;

    private Set<Tag> tags;
    private Instant createdDate;

    public Note(String title, String text, Set<Tag> tags) {
        this.createdDate = Instant.now();
        this.title = title;
        this.text = text;
        this.tags = tags;
    }
}