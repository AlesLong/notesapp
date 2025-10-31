package com.yevbes.notesapp.controller;

import com.yevbes.notesapp.dto.DeleteResponse;
import com.yevbes.notesapp.dto.NoteDetailResponse;
import com.yevbes.notesapp.dto.NoteRequest;
import com.yevbes.notesapp.dto.NoteResponse;
import com.yevbes.notesapp.model.Note;
import com.yevbes.notesapp.model.Tag;
import com.yevbes.notesapp.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity<Note> createNote(@Valid @RequestBody NoteRequest request) {
        Note note = noteService.createNote(request);
        return ResponseEntity.ok(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
            @PathVariable String id,
            @Valid @RequestBody NoteRequest request) {
        Note note = noteService.updateNote(id, request);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        DeleteResponse response = new DeleteResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<NoteResponse>> getAllNotes(
            @RequestParam(required = false) List<Tag> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NoteResponse> notes = noteService.getAllNotes(tags, pageable);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDetailResponse> getNoteById(@PathVariable String id) {
        NoteDetailResponse note = noteService.getNoteById(id);
        return ResponseEntity.ok(note);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Integer>> getNoteStats(@PathVariable String id) {
        NoteDetailResponse note = noteService.getNoteById(id);
        return ResponseEntity.ok(note.getStats());
    }
}