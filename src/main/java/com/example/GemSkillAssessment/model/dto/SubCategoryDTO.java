package com.example.GemSkillAssessment.model.dto;

import java.util.List;

public class SubCategoryDTO {
    private Integer id;
    private String name;
    private List<Integer> idBaseItem;

    public SubCategoryDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getIdBaseItems() {
        return idBaseItem;
    }

    public void setIdBaseItems(List<Integer> idBaseItem) {
        this.idBaseItem = idBaseItem;
    }
}
