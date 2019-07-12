package com.example.GemSkillAssessment.model.dto;

public class BaseItemDTO {
    private Integer id;
    private String name;
    private String type;
    private Integer idSub;

    public BaseItemDTO() {
    }

    public BaseItemDTO(Integer id, String name, String type, Integer idSub) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.idSub = idSub;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIdSub() {
        return idSub;
    }

    public void setIdSub(Integer idSub) {
        this.idSub = idSub;
    }
}
