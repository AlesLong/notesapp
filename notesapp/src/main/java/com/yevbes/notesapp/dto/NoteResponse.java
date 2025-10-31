package com.yevbes.notesapp.dto;

import com.yevbes.notesapp.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {

    private String id;
    private String title;
    private Instant createdDate;
    private Set<Tag> tags;
}