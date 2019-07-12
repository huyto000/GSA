package com.example.GemSkillAssessment.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "form")
public class Form implements Serializable {
    private static final long serialVersionUID = 40L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "enable")
    private boolean enable;
    @Column(name = "finishReview")
    private Boolean finishReview;
    @Column(name = "finish")
    private Boolean finish;
    @Column(name = "creatDate")
    private Date creatDate;
    @Column(name = "updateDate")
    private Date editDate;
    @Column(name = "finishDate")
    private Date finishDate;
    @Column(name = "reviewDate")
    private Date reviewDate;
    @Column(name = "remindReviewSelf")
    private Boolean remindReviewSelf;
    @Column(name = "remindReviewUser")
    private Boolean remindReviewUser;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "form")
    private Set<FormBaseItem> formBaseItemSet;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private Period period;

    public Form(boolean enable, Date creatDate, User user, Period period, Template template) {
        this.enable = enable;
        this.creatDate = creatDate;
        this.user = user;
        this.period = period;
        this.template = template;
    }

    public Form() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<FormBaseItem> getFormBaseItemSet() {
        return formBaseItemSet;
    }

    public void setFormBaseItemSet(Set<FormBaseItem> formBaseItemSet) {
        this.formBaseItemSet = formBaseItemSet;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Date getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(Date creatDate) {
        this.creatDate = creatDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Boolean getFinishReview() {
        return finishReview;
    }

    public void setFinishReview(Boolean finishReview) {
        this.finishReview = finishReview;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Boolean getRemindReviewSelf() {
        return remindReviewSelf;
    }

    public void setRemindReviewSelf(Boolean remindReviewSelf) {
        this.remindReviewSelf = remindReviewSelf;
    }

    public Boolean getRemindReviewUser() {
        return remindReviewUser;
    }

    public void setRemindReviewUser(Boolean remindReviewUser) {
        this.remindReviewUser = remindReviewUser;
    }

}
