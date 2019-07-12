package com.example.GemSkillAssessment.enumerted;

public enum EKeyFunction {
    FILL_EDIT("User is filling or editing."),
    REVIEW("User is reviewing."),
    VIEW_REPORT("Admin view all report."),
    GET_SUM_POINT_CATE("User view sum point of category."),
    GET_POINT_OF_FORM("User view sum point of sub category.");

    private String label;

    private EKeyFunction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public String getStatus() {
        return this.name();
    }
}
