package com.example.GemSkillAssessment.model;

import com.example.GemSkillAssessment.enumerted.EJobTitle;
import org.hibernate.annotations.CollectionType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "template")
public class Template implements Serializable {
    private static final long serialVersionUID = 40L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "jobTitle")
    @Enumerated(EnumType.STRING)
    private EJobTitle EJobTitle;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "template_category",
            joinColumns = @JoinColumn(name = "template_id")
            , inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @OrderBy("id")
    private Set<Category> categories  = new HashSet<>();


    public Template(){}
    public Template(com.example.GemSkillAssessment.enumerted.EJobTitle EJobTitle, Set<Category> categories, Period period) {
        this.EJobTitle = EJobTitle;
        this.categories = categories;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.example.GemSkillAssessment.enumerted.EJobTitle getEJobTitle() {
        return EJobTitle;
    }

    public void setEJobTitle(com.example.GemSkillAssessment.enumerted.EJobTitle EJobTitle) {
        this.EJobTitle = EJobTitle;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }


}

