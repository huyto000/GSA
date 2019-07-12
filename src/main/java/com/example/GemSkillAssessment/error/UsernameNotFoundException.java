package com.example.GemSkillAssessment.error;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {
        super("Username Not found");
    }
}

