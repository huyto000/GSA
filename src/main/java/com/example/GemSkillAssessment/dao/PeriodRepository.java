package com.example.GemSkillAssessment.dao;

import com.example.GemSkillAssessment.model.Period;
import org.springframework.data.repository.CrudRepository;

public interface PeriodRepository extends CrudRepository<Period, Integer> {
    Period findById(int idPeriod);

    Period findByEnabledTrue();

    Period findTopByOrderByStartDateDesc();
}