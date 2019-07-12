package com.example.GemSkillAssessment.model.dto;

public class ErrorDto {
    private int errorId;
    private String message;

    public ErrorDto() {
    }

    public ErrorDto(int errorId, String message) {
        this.errorId = errorId;
        this.message = message;
    }

    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
