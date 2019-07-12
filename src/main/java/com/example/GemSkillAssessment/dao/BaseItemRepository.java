package com.example.GemSkillAssessment.dao;

import com.example.GemSkillAssessment.enumerted.EBaseItemType;
import com.example.GemSkillAssessment.model.BaseItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface BaseItemRepository extends CrudRepository<BaseItem, String> {
    BaseItem findById(Integer id);

    Set<BaseItem> findAllByIdIn(List<Integer> id);

    Set<BaseItem> findAll();

    BaseItem findByNameAndType(String name, EBaseItemType eBaseItemType);

    BaseItem findByName(String name);

    Set<BaseItem> findAllByOrderById();

    Set<BaseItem> findByType(EBaseItemType type);

    void delete(BaseItem baseItem);

    void deleteById(Integer id);
}

