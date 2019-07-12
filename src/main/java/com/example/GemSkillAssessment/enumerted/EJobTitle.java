package com.example.GemSkillAssessment.enumerted;

import java.io.Serializable;

public enum EJobTitle implements Serializable {
    DEVELOPER(1, "Developer"),
    SENIOR_DEVELOPER(2, ("Senior depverloper")),
    TECHNICAL_LEADER(3, "Technical leader"),
    TECHNICAL_ARCHITECT(4, "Technical architect"),
    DELIVERY_MANAGER(5, "Delivery"),
    ASSISTANT_TECHNICAL_PROJECT_MANAGER(6, "Assistant technical project manager"),
    DOMESTIC_PORTFOLIO_MANAGER(7, "Domestic portfolio manager"),
    PROJECT_MANAGER(8, "Project manager"),
    BUSINESS_ANALYST(9, "Business analyst"),
    BUSINESS_ANALYST_LEAD(10, "Business analyst lead"),
    QUALITY_CONTROL(11, "Quality control"),
    QUALITY_CONTROL_LEAD(12, "Quality control lead"),
    TESTER(13, "Tester"),
    SENIOR_TESTER(14, "Senior tester"),
    DESIGNER(15, "Designer"),
    JAPANESE_COMMUNICATOR(16, "Japanese communicator"),
    CEO(17, "CEO"),
    COO(18, "COO"),
    ADMIN(19, "ADMIN");

    private int label;
    private String name;

    private EJobTitle(int label, String name) {
        this.label = label;
        this.name = name;
    }

    public int getLabel() {
        return this.label;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
