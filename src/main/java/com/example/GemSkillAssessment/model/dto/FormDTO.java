package com.example.GemSkillAssessment.model.dto;

import com.example.GemSkillAssessment.enumerted.EJobTitle;
import com.example.GemSkillAssessment.model.Category;

import java.util.List;
import java.util.Set;

public class FormDTO {
    private Integer idForm;
    private Set<Category> categorySet;
    private Integer idUser;
    private String name;
    private String nameUser;
    private String idEmployee;
    private EJobTitle eJobTitle;
    private String nameSupervisor;
    private boolean remindReviewSelf;
    private boolean remindReviewUser;
    private boolean finishReview;
    private boolean finish;
    private boolean fill;
    private boolean edit;
    private List<FormBaseItemDTO> formBaseItemDTOs;
    private List<SumPointCategoryDTO> sumPointCategoryDTOList;
    private List<SumPointSubDTO> sumPointSubDTOList;

    public FormDTO() {
    }

    public Integer getIdForm() {
        return idForm;
    }

    public void setIdForm(Integer idForm) {
        this.idForm = idForm;
    }

    public Set<Category> getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(Set<Category> categorySet) {
        this.categorySet = categorySet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FormBaseItemDTO> getFormBaseItemDTOs() {
        return formBaseItemDTOs;
    }

    public void setFormBaseItemDTOs(List<FormBaseItemDTO> formBaseItemDTOs) {
        this.formBaseItemDTOs = formBaseItemDTOs;
    }

    public List<SumPointCategoryDTO> getSumPointCategoryDTOList() {
        return sumPointCategoryDTOList;
    }

    public void setSumPointCategoryDTOList(List<SumPointCategoryDTO> sumPointCategoryDTOList) {
        this.sumPointCategoryDTOList = sumPointCategoryDTOList;
    }

    public boolean isFinishReview() {
        return finishReview;
    }

    public void setFinishReview(boolean finishReview) {
        this.finishReview = finishReview;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public List<SumPointSubDTO> getSumPointSubDTOList() {
        return sumPointSubDTOList;
    }

    public void setSumPointSubDTOList(List<SumPointSubDTO> sumPointSubDTOList) {
        this.sumPointSubDTOList = sumPointSubDTOList;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public EJobTitle geteJobTitle() {
        return eJobTitle;
    }

    public void seteJobTitle(EJobTitle eJobTitle) {
        this.eJobTitle = eJobTitle;
    }

    public String getNameSupervisor() {
        return nameSupervisor;
    }

    public void setNameSupervisor(String nameSupervisor) {
        this.nameSupervisor = nameSupervisor;
    }

    public boolean isRemindReviewSelf() {
        return remindReviewSelf;
    }

    public void setRemindReviewSelf(boolean remindReviewSelf) {
        this.remindReviewSelf = remindReviewSelf;
    }

    public boolean isRemindReviewUser() {
        return remindReviewUser;
    }

    public void setRemindReviewUser(boolean remindReviewUser) {
        this.remindReviewUser = remindReviewUser;
    }
}
