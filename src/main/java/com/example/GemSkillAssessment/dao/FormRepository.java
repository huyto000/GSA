package com.example.GemSkillAssessment.dao;

import com.example.GemSkillAssessment.model.Form;
import com.example.GemSkillAssessment.model.Period;
import com.example.GemSkillAssessment.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FormRepository extends CrudRepository<Form, String> {
    Form findById(Integer id);

    List<Form> findAll();

    List<Form> findByPeriod(Period period);

    //    @Query("SELECT f FROM Form f WHERE f.enable = 1 AND f.period = ?1 AND f.user = ?2")
    Form findByEnableIsTrueAndPeriodAndUser(Period period, User user);


}
