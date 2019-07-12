package com.example.GemSkillAssessment.model;

import com.example.GemSkillAssessment.enumerted.EJobTitle;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 39L;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_permission",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")}
    )
    Set<Permission> permissions;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @NotNull(message = "id Employee must not be null")
    @Column(name = "idEmployee", unique = true)
    private String idEmployee;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @NotNull(message = "jobTitle must not be null")
    @Column(name = "jobTitle")
    @Enumerated(EnumType.STRING)
    private EJobTitle EJobTitle;
    @NotNull(message = "joiningDate must not be null")
    @Column(name = "joiningDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date joiningDate;
    @NotNull(message = "joiningDate must not be null")
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "enabled")
    private boolean isEnabled;
    @Column(name = "isAdmin")
    private boolean isAdmin;
    @Column(name = "tokenVerify")
    private String tokenVerify;

    @Column(name = "supervised_id")
    private int supervisedId;

    @Column(name = "supervised_name")
    private String supervisedName;

    @Column(name = "invited")
    private boolean invited;


    @Column(name = "isLeft")

    private boolean isLeft;

//    @JsonIgnore
//    @ManyToMany
//    @JoinTable(
//            name = "user_role",
//            joinColumns = @JoinColumn(name = "user_id")
//            , inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles;

    public User() {

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEJobTitle() {
        return EJobTitle.getLabel();
    }

    public void setEJobTitle(EJobTitle EJobTitle) {
        this.EJobTitle = EJobTitle;
    }

    public EJobTitle getVaEJobTitle() {
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getTokenVerify() {
        return tokenVerify;
    }

    public void setTokenVerify(String tokenVerify) {
        this.tokenVerify = tokenVerify;
    }

    public String getSupervisedName() {
        return supervisedName;
    }

    public void setSupervisedName(String supervisedName) {
        this.supervisedName = supervisedName;
    }

//
//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }

    public int getSupervisedId() {
        return supervisedId;
    }

    public void setSupervisedId(int supervisedId) {
        this.supervisedId = supervisedId;
    }

    public boolean isInvited() {
        return invited;
    }

    public void setInvited(boolean invited) {
        this.invited = invited;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
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
        this.isLeft = left;
    }

}

