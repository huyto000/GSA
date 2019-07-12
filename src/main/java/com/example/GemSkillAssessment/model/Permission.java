package com.example.GemSkillAssessment.model;

import com.example.GemSkillAssessment.enumerted.EPermission;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "permission")
public class Permission implements Serializable {
    private static final long serialVersionUID = 40L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EPermission type;
    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private Set<User> users;

    public Permission() {
    }

    public Permission(EPermission ePermission) {
        this.type = ePermission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EPermission getType() {
        return type;
    }

    public void setType(EPermission type) {
        this.type = type;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
