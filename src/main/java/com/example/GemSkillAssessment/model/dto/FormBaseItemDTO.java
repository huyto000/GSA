package com.example.GemSkillAssessment.model.dto;

import com.example.GemSkillAssessment.enumerted.EBaseItemType;

public class FormBaseItemDTO {
    private Integer baseItemId;
    private EBaseItemType type;
    private String comment;
    private String commentReview;
    private Integer point;
    private Integer pointReview;
    private String baseItemName;

    public Integer getBaseItemId() {
        return baseItemId;
    }

    public void setBaseItemId(Integer baseItemId) {
        this.baseItemId = baseItemId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getCommentReview() {
        return commentReview;
    }

    public void setCommentReview(String commentReview) {
        this.commentReview = commentReview;
    }

    public String getBaseItemName() {
        return baseItemName;
    }

    public void setBaseItemName(String baseItemName) {
        this.baseItemName = baseItemName;
    }

    public EBaseItemType getType() {
        return type;
    }

    public void setType(EBaseItemType type) {
        this.type = type;
    }
}

