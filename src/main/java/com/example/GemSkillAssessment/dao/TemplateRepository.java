package com.example.GemSkillAssessment.dao;

import com.example.GemSkillAssessment.model.Template;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TemplateRepository extends CrudRepository<Template, String> {
    Template findById(Integer id);
    //Set<Template> findByPeriodIsNull();
    Set<Template> findAll();

    //Set<Template> findAllByPeriodId(Integer id);


//    Set<Template>  findByEJobTitle(EJobTitle eJobTitle);


    void deleteById(Integer id);

}
