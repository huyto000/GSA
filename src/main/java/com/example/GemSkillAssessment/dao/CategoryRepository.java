package com.example.GemSkillAssessment.dao;

import com.example.GemSkillAssessment.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Optional<Category> findById(Integer id);

    Category findByName(String name);

    Set<Category> findAll();

    void deleteById(Integer id);
}
