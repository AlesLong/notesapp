package com.yevbes.notesapp.dto;

import lombok.Data;

@Data
public class DeleteResponse {
    private String message;
    private String deletedId;

    public DeleteResponse(String deletedId) {
        this.message = "Note successfully deleted";
        this.deletedId = deletedId;
    }
}