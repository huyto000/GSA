package com.example.GemSkillAssessment.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "jwt")
public class JWT implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "code")
    private String code;
    @Column(name = "lockJwt")
    private boolean lockJwt;
    @Column(name = "dateUpdate")
    private Date dateUpdate;

    public JWT() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isLockJwt() {
        return lockJwt;
    }

    public void setLockJwt(boolean lockJwt) {
        this.lockJwt = lockJwt;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }
}

