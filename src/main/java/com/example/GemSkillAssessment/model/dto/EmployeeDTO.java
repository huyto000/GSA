package com.example.GemSkillAssessment.model.dto;

import com.example.GemSkillAssessment.enumerted.EJobTitle;
import com.example.GemSkillAssessment.model.Permission;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class EmployeeDTO {

    Set<Permission> permissions = new HashSet<>();
    private int id;
    private String idEmployee;
    private String name;
    private EJobTitle EJobTitle;
    private Date joiningDate;
    private String email;
    private int supervisedId;
    private String supervisedName;
    private boolean invited;
    private String tokenVerify;
    private boolean isEnabled;
    private boolean fill;
    private boolean edit;
    private boolean review;


    private boolean isAdmin;
    private boolean isLeft;

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public com.example.GemSkillAssessment.enumerted.EJobTitle getVaEJobTitle() {
        return EJobTitle;
    }

    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSupervisedId() {
        return supervisedId;
    }

    public void setSupervisedId(int supervisedId) {
        this.supervisedId = supervisedId;
    }

    public String getSupervisedName() {
        return supervisedName;
    }

    public void setSupervisedName(String supervisedName) {
        this.supervisedName = supervisedName;
    }

    public boolean isInvited() {
        return invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }


    public String getTokenVerify() {
        return tokenVerify;
    }

    public void setTokenVerify(String tokenVerify) {
        this.tokenVerify = tokenVerify;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getEJobTitle() {
        return EJobTitle.getLabel();
    }

    public void setEJobTitle(com.example.GemSkillAssessment.enumerted.EJobTitle EJobTitle) {
        this.EJobTitle = EJobTitle;
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

    public boolean isReview() {
        return review;
    }

    public void setReview(boolean review) {
        this.review = review;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

}