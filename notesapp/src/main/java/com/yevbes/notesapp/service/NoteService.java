package com.yevbes.notesapp.service;

import com.yevbes.notesapp.dto.NoteDetailResponse;
import com.yevbes.notesapp.dto.NoteRequest;
import com.yevbes.notesapp.dto.NoteResponse;
import com.yevbes.notesapp.exception.NoteNotFoundException;
import com.yevbes.notesapp.model.Note;
import com.yevbes.notesapp.model.Tag;
import com.yevbes.notesapp.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note createNote(NoteRequest request) {
        Note note = new Note(request.getTitle(), request.getText(), request.getTags());
        return noteRepository.save(note);
    }

    public Note updateNote(String id, NoteRequest request) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + id));

        note.setTitle(request.getTitle());
        note.setText(request.getText());
        note.setTags(request.getTags());

        return noteRepository.save(note);
    }

    public void deleteNote(String id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException("Note not found with id: " + id);
        }
        noteRepository.deleteById(id);
    }

    public Page<NoteResponse> getAllNotes(List<Tag> tags, Pageable pageable) {
        Page<Note> notes;
        if (tags != null && !tags.isEmpty()) {
            notes = noteRepository.findByTagsIn(tags, pageable);
        } else {
            notes = noteRepository.findAllByOrderByCreatedDateDesc(pageable);
        }

        return notes.map(this::convertToNoteResponse);
    }

    public NoteDetailResponse getNoteById(String id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + id));

        return convertToNoteDetailResponse(note);
    }

    public Map<String, Integer> calculateStats(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new HashMap<>();
        }

        String cleanedText = text.replaceAll("[^a-zA-Z\\s]", "").toLowerCase();
        String[] words = cleanedText.split("\\s+");

        return Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.summingInt(word -> 1)
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private NoteResponse convertToNoteResponse(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getCreatedDate(),
                note.getTags()
        );
    }

    private NoteDetailResponse convertToNoteDetailResponse(Note note) {
        NoteDetailResponse response = new NoteDetailResponse();
        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setText(note.getText());
        response.setCreatedDate(note.getCreatedDate());
        response.setTags(note.getTags());
        response.setStats(calculateStats(note.getText()));

        return response;
    }
}