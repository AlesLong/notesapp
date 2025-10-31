package com.yevbes.notesapp.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.ZonedDateTime;
import java.time.ZoneId;
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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime createdDate;

    public Note(String title, String text, Set<Tag> tags) {
        this.createdDate = ZonedDateTime.now(ZoneId.systemDefault());
        this.title = title;
        this.text = text;
        this.tags = tags;
    }
}