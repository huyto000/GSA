package com.example.GemSkillAssessment.dao;

import com.example.GemSkillAssessment.model.SubCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface SubCategoryRepository extends CrudRepository<SubCategory, String> {
    SubCategory findById(Integer id);

    SubCategory findByName(String name);

    Set<SubCategory> findAll();

    void delete(SubCategory subCategory);


    @Query(value = "select * from subcategory where subcategory.id not in\n" +
            "(select subcategory.id from subcategory inner join category_subcategory on subcategory.id = category_subcategory.subcategory_id where category_subcategory.category_id = ?1) ", nativeQuery = true)
    Set<SubCategory> findSubCategoriesNotInCategory(Integer id);

    void deleteById(Integer id);

}
