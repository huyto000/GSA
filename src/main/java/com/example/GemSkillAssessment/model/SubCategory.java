package com.example.GemSkillAssessment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subcategory")
public class SubCategory implements Serializable {
    private static final long serialVersionUID = 40L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;


    /*@JsonIgnore
    @ManyToMany(mappedBy = "subCategories",fetch = FetchType.LAZY)
    private Set<Category> categories;*/

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "subcategory_baseitem",
            joinColumns = @JoinColumn(name = "subcategory_id")
            , inverseJoinColumns = @JoinColumn(name = "baseitem_id")
    )
    @OrderBy("id")
    private Set<BaseItem> baseItems = new HashSet<>();

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

    /*public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }*/

    public Set<BaseItem> getBaseItems() {
        return baseItems;
    }

    public void setBaseItems(Set<BaseItem> baseItems) {
        this.baseItems = baseItems;
    }


}


