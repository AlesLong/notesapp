package com.yevbes.notesapp;

import com.yevbes.notesapp.exception.NoteNotFoundException;
import com.yevbes.notesapp.model.Note;
import com.yevbes.notesapp.model.Tag;
import com.yevbes.notesapp.dto.NoteRequest;
import com.yevbes.notesapp.repository.NoteRepository;
import com.yevbes.notesapp.service.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    public void testCreateNote() {
        NoteRequest request = new NoteRequest();
        request.setTitle("Test Title");
        request.setText("Test text content");
        request.setTags(Set.of(Tag.PERSONAL));

        Note note = new Note("Test Title", "Test text content", Set.of(Tag.PERSONAL));
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        Note result = noteService.createNote(request);

        assertNotNull(result);
        assertNotNull(result.getCreatedDate());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test text content", result.getText());
        assertTrue(result.getTags().contains(Tag.PERSONAL));
    }

    @Test
    public void testUpdateNote_WhenNoteExists() {
        String noteId = "1";
        Note existingNote = new Note("Old Title", "Old Text", Set.of(Tag.PERSONAL));
        existingNote.setId(noteId);

        NoteRequest updateRequest = new NoteRequest();
        updateRequest.setTitle("New Title");
        updateRequest.setText("New Text");
        updateRequest.setTags(Set.of(Tag.BUSINESS));

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenReturn(existingNote);

        Note result = noteService.updateNote(noteId, updateRequest);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Text", result.getText());
        assertTrue(result.getTags().contains(Tag.BUSINESS));
    }

    @Test
    public void testUpdateNote_WhenNoteNotFound() {
        String noteId = "999";
        NoteRequest updateRequest = new NoteRequest();
        updateRequest.setTitle("New Title");
        updateRequest.setText("New Text");

        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> {
            noteService.updateNote(noteId, updateRequest);
        });
    }

    @Test
    public void testDeleteNote_WhenNoteExists() {
        String noteId = "1";
        when(noteRepository.existsById(noteId)).thenReturn(true);

        noteService.deleteNote(noteId);

        verify(noteRepository, times(1)).deleteById(noteId);
    }

    @Test
    public void testDeleteNote_WhenNoteNotFound() {
        String noteId = "999";
        when(noteRepository.existsById(noteId)).thenReturn(false);

        assertThrows(NoteNotFoundException.class, () -> {
            noteService.deleteNote(noteId);
        });
    }

    @Test
    public void testGetNoteById_WhenNoteNotFound() {
        String noteId = "999";
        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> {
            noteService.getNoteById(noteId);
        });
    }

    @Test
    public void testCalculateStats() {
        String text = "note is just a note";
        var stats = noteService.calculateStats(text);

        assertEquals(4, stats.size());
        assertEquals(2, stats.get("note"));
        assertEquals(1, stats.get("is"));
        assertEquals(1, stats.get("just"));
        assertEquals(1, stats.get("a"));
    }
}