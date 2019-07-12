package com.example.GemSkillAssessment.service.manage;

import com.example.GemSkillAssessment.dao.CategoryRepository;
import com.example.GemSkillAssessment.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Set<Category> findAll() {
        return (Set<Category>) categoryRepository.findAll();
    }

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    public void save(Category category) {
        //if(category.getId() == null ) throw new NotFoundException("Id must not be null");
        categoryRepository.save(category);
    }

    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

}
