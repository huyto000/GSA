package com.example.GemSkillAssessment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "formBaseitem")
public class FormBaseItem implements Serializable {
    private static final long serialVersionUID = 40L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "point")
    private Integer point;
    @Column(name = "pointReview")
    private Integer pointReview;
    @Column(name = "comment")
    private String comment;
    @Column(name = "commentReview")
    private String commentReview;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "form_id")
    private Form form;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "baseItem_id")
    @OrderBy("id")
    private BaseItem baseItemForm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Integer getPointReview() {
        return pointReview;
    }

    public void setPointReview(Integer pointReview) {
        this.pointReview = pointReview;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public BaseItem getBaseItemForm() {
        return baseItemForm;
    }

    public void setBaseItemForm(BaseItem baseItemForm) {
        this.baseItemForm = baseItemForm;
    }

    public String getCommentReview() {
        return commentReview;
    }

    public void setCommentReview(String commentReview) {
        this.commentReview = commentReview;
    }
}

