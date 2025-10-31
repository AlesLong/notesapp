package com.yevbes.notesapp.dto;

import com.yevbes.notesapp.model.Tag;
import lombok.Data;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Data
public class NoteDetailResponse {

    private String id;
    private String title;
    private String text;
    private Instant createdDate;
    private Set<Tag> tags;
    private Map<String, Integer> stats;
}