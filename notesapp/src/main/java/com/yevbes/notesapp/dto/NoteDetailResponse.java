package com.yevbes.notesapp.dto;

import com.yevbes.notesapp.model.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

@Data
public class NoteDetailResponse {
    private String id;
    private String title;
    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime createdDate;

    private Set<Tag> tags;
    private Map<String, Integer> stats;
}