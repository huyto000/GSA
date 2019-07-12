package com.example.GemSkillAssessment.error;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException() {
        super("Duplicate Entry Exception");
    }
}
