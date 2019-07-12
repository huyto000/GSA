package com.example.GemSkillAssessment.dao;


import com.example.GemSkillAssessment.model.BaseItem;
import com.example.GemSkillAssessment.model.Form;
import com.example.GemSkillAssessment.model.FormBaseItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FormBaseItemRepository extends CrudRepository<FormBaseItem, String> {
    FormBaseItem findByFormAndBaseItemForm(Form form, BaseItem baseItemForm);

    List<FormBaseItem> findAll();

    List<FormBaseItem> findAllById(Integer id);

    List<FormBaseItem> findAllByFormId(Integer formId);
}
