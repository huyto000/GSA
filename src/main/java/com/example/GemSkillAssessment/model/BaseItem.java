package com.example.GemSkillAssessment.model;

import com.example.GemSkillAssessment.enumerted.EBaseItemType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "baseitem")
public class BaseItem implements Serializable {
    private static final long serialVersionUID = 40L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    private EBaseItemType type;

    /*@JsonIgnore
    @ManyToMany(mappedBy = "baseItems")
    private Set<SubCategory> subCategories;*/

    @JsonIgnore
    @OneToMany(mappedBy = "baseItemForm",fetch = FetchType.EAGER)
    private Set<FormBaseItem> formBaseItemSet;

    private FormBaseItem formBaseItem;

    public BaseItem() {
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

    public EBaseItemType getType() {
        return type;
    }

    public void setType(EBaseItemType type) {
        this.type = type;
    }

    /*public Set<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Set<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }*/

    public Set<FormBaseItem> getFormBaseItemSet() {
        return formBaseItemSet;
    }

    public void setFormBaseItemSet(Set<FormBaseItem> formBaseItemSet) {
        this.formBaseItemSet = formBaseItemSet;
    }

    public FormBaseItem getFormBaseItem() {
        return formBaseItem;
    }

    public void setFormBaseItem(FormBaseItem formBaseItem) {
        this.formBaseItem = formBaseItem;
    }

}
