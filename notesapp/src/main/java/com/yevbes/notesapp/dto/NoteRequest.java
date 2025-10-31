package com.yevbes.notesapp.dto;

import com.yevbes.notesapp.model.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class NoteRequest {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Text is mandatory")
    private String text;

    private Set<Tag> tags;
}