package com.example.GemSkillAssessment.model.dto;

import java.util.List;

public class SumPointCategoryDTO {
    private Integer id;
    private String name;
    private Integer selfAssessment;
    private Integer supervisorAssessment;
    private List<SumPointSubDTO> sumPointSubDTOList;

    public SumPointCategoryDTO() {
    }

    public SumPointCategoryDTO(Integer id, String name, Integer selfAssessment, Integer supervisorAssessment, List<SumPointSubDTO> sumPointSubDTOList) {
        this.id = id;
        this.name = name;
        this.selfAssessment = selfAssessment;
        this.supervisorAssessment = supervisorAssessment;
        this.sumPointSubDTOList = sumPointSubDTOList;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<SumPointSubDTO> getSumPointSubDTOList() {
        return sumPointSubDTOList;
    }

    public void setSumPointSubDTOList(List<SumPointSubDTO> sumPointSubDTOList) {
        this.sumPointSubDTOList = sumPointSubDTOList;
    }
}

