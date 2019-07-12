package com.example.GemSkillAssessment.model.dto;

import java.util.List;

public class UserDto {
    private String email;
    private String token;
    private String idEmployee;
    private List<String> idEmployees;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public List<String> getIdEmployees() {
        return idEmployees;
    }

    public void setIdEmployees(List<String> idEmployees) {
        this.idEmployees = idEmployees;
    }
}
