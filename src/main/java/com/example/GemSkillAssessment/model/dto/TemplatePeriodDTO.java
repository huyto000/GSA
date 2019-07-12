package com.example.GemSkillAssessment.model.dto;

import com.example.GemSkillAssessment.model.Template;

import java.io.Serializable;
import java.util.Set;

public class TemplatePeriodDTO implements Serializable {
    private static final long serialVersionUID = 1314862760591043352L;
    private int periodId;
    private Set<Template> templates ;

    public TemplatePeriodDTO(){}

    public TemplatePeriodDTO(int periodId, Set<Template> templates) {
        this.periodId = periodId;
        this.templates = templates;
    }

    public int getPeriodId() {
        return periodId;
    }

    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }

    public Set<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(Set<Template> templates) {
        this.templates = templates;
    }


}
