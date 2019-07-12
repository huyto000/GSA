package com.example.GemSkillAssessment.model.dto;

public class DataDTO<T> {
    private T data;
    private String messge;

    public DataDTO(T data, String messge) {
        this.data = data;
        this.messge = messge;
    }

    public DataDTO(String messge) {
        this.messge = messge;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessge() {
        return messge;
    }

    public void setMessge(String messge) {
        this.messge = messge;
    }

}
