package com.yevbes.notesapp;

import com.yevbes.notesapp.exception.NoteNotFoundException;
import com.yevbes.notesapp.model.Note;
import com.yevbes.notesapp.model.Tag;
import com.yevbes.notesapp.dto.NoteRequest;
import com.yevbes.notesapp.dto.NoteResponse;
import com.yevbes.notesapp.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NoteService noteService;

    @Test
    public void testCreateNote_Success() throws Exception {
        Note note = new Note("Test Title", "Test text", Set.of(Tag.BUSINESS));
        note.setId("1");

        when(noteService.createNote(any(NoteRequest.class))).thenReturn(note);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Test Title",
                                    "text": "Test text",
                                    "tags": ["BUSINESS"]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Test Title"));
    }

    @Test
    public void testCreateNote_ValidationError() throws Exception {
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "",
                                    "text": "Test text",
                                    "tags": ["BUSINESS"]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    public void testCreateNote_WithInvalidTag() throws Exception {
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Test Title",
                                    "text": "Test text",
                                    "tags": ["INVALID_TAG"]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid tag value. Allowed values are: BUSINESS, PERSONAL, IMPORTANT"));
    }

    @Test
    public void testGetNoteById_NotFound() throws Exception {
        when(noteService.getNoteById("999"))
                .thenThrow(new NoteNotFoundException("Note not found with id: 999"));

        mockMvc.perform(get("/api/notes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Note not found with id: 999"));
    }

    @Test
    public void testUpdateNote_NotFound() throws Exception {
        when(noteService.updateNote(eq("999"), any(NoteRequest.class)))
                .thenThrow(new NoteNotFoundException("Note not found with id: 999"));

        mockMvc.perform(put("/api/notes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "New Title",
                                    "text": "New Text",
                                    "tags": ["BUSINESS"]
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Note not found with id: 999"));
    }

    @Test
    public void testDeleteNote_NotFound() throws Exception {
        doThrow(new NoteNotFoundException("Note not found with id: 999"))
                .when(noteService).deleteNote("999");

        mockMvc.perform(delete("/api/notes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Note not found with id: 999"));
    }

    @Test
    public void testGetAllNotes_Success() throws Exception {
        NoteResponse response1 = new NoteResponse("1", "Title 1",
                Instant.now(), Set.of(Tag.BUSINESS));
        NoteResponse response2 = new NoteResponse("2", "Title 2",
                Instant.now(), Set.of(Tag.PERSONAL));

        Page<NoteResponse> page = new PageImpl<>(List.of(response1, response2));

        when(noteService.getAllNotes(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].createdDate").exists())
                .andExpect(jsonPath("$.content[1].createdDate").exists());
    }

    @Test
    public void testGetNoteById_Success() throws Exception {
        com.yevbes.notesapp.dto.NoteDetailResponse response = new com.yevbes.notesapp.dto.NoteDetailResponse();
        response.setId("1");
        response.setTitle("Test Title");
        response.setText("Test text");
        response.setCreatedDate(Instant.now());

        when(noteService.getNoteById("1")).thenReturn(response);

        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.createdDate").exists());
    }

    @Test
    public void testDeleteNote_Success() throws Exception {
        doNothing().when(noteService).deleteNote("1");

        mockMvc.perform(delete("/api/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Note successfully deleted"))
                .andExpect(jsonPath("$.deletedId").value("1"));
    }
}