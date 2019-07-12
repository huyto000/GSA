package com.example.GemSkillAssessment.model.dto;

public class SumPointSubDTO {
    private String name;
    private Integer selfAssessment;
    private Integer supervisorAssessment;
    private Integer idCategory;

    public SumPointSubDTO() {
    }

    public SumPointSubDTO(Integer idCategory, String name, Integer selfAssessment, Integer supervisorAssessment) {
        this.idCategory = idCategory;
        this.name = name;
        this.selfAssessment = selfAssessment;
        this.supervisorAssessment = supervisorAssessment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSelfAssessment() {
        return selfAssessment;
    }

    public void setSelfAssessment(Integer selfAssessment) {
        this.selfAssessment = selfAssessment;
    }

    public Integer getSupervisorAssessment() {
        return supervisorAssessment;
    }

    public void setSupervisorAssessment(Integer supervisorAssessment) {
        this.supervisorAssessment = supervisorAssessment;
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }
}
