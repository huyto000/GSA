package com.example.GemSkillAssessment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "period")
public class Period implements Serializable {
    private static final long serialVersionUID = 1749743265286280491L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @NotNull(message = "startDate must not be null")
    @Column(name = "startDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;


    @Column(name = "finishDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishDate;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "period", cascade = {CascadeType.REMOVE})
    private Set<Form> listForm = new HashSet<>();


    public Period() {
    }

    public Period(Date startDate, Date finishDate, boolean isEnabled) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Form> getListForm() {
        return listForm;
    }

    public void setListForm(Set<Form> listForm) {
        this.listForm = listForm;
    }

}
