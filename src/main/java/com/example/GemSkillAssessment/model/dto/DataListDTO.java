package com.example.GemSkillAssessment.model.dto;

import java.util.List;
import java.util.Set;

public class DataListDTO<T> {
    private Set<T> data;
    private String message;
    private List<T> datas;

    public DataListDTO(Set<T> data, String message) {
        this.data = data;
        this.message = message;
    }

    public DataListDTO(String message, List<T> datas) {
        this.message = message;
        this.datas = datas;
    }

    public Set<T> getData() {
        return data;
    }

    public void setData(Set<T> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}

