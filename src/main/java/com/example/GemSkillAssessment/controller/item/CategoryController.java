package com.example.GemSkillAssessment.controller.item;

import com.example.GemSkillAssessment.dao.CategoryRepository;
import com.example.GemSkillAssessment.dao.SubCategoryRepository;
import com.example.GemSkillAssessment.error.NotAcceptableException;
import com.example.GemSkillAssessment.model.Category;
import com.example.GemSkillAssessment.model.SubCategory;
import com.example.GemSkillAssessment.service.manage.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.NotAcceptableStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@CrossOrigin()
@RestController
@RequestMapping("/categories")
@PreAuthorize("hasRole('ADMIN')")

public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        Set<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        Optional<Category> category = categoryService.findById(id);
        return ResponseEntity.ok(category.get());
    }

    @PostMapping("")
    public ResponseEntity<?> addCategory(@RequestBody() Category category) {
        Category category1 = categoryRepository.findByName(category.getName());
        if(category1!=null) throw new NotAcceptableException("Name is exist");
        categoryService.save(category);
        return ResponseEntity.ok("Done!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Integer id, @RequestBody Category category) {
        Optional<Category> updateCategory = categoryService.findById(id);
        Category category1 = categoryRepository.findByName(category.getName());
        if(category1!=null && category1.getId()!=id) throw new NotAcceptableException("Name is exist");
        updateCategory.get().setId(id);
        updateCategory.get().setName(category.getName());
        categoryService.save(updateCategory.get());
        return ResponseEntity.ok("Update Successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok("Done!");
    }

    // add subcate to a cate with id
    @PostMapping("/{id}/subcategories")
    public ResponseEntity<?> addSubToCate(@PathVariable("id") Integer id, @RequestBody List<Integer> subCategoriesId) {
        Category category = categoryService.findById(id).get();
        for (int subId : subCategoriesId) {
            category.getSubCategories().add(subCategoryRepository.findById(subId));
        }
        categoryService.save(category);
        return ResponseEntity.ok("Add subcategory done!");
    }

    //get list subcate by cateid
    @GetMapping("/subcategories")
    public ResponseEntity<Set<SubCategory>> getSubByCatId(@RequestParam("id") Integer id) {
        Category category = categoryService.findById(id).get();
        return ResponseEntity.ok(category.getSubCategories());
    }

    @DeleteMapping("/{id}/subcategories")
    public ResponseEntity<?> deleteSubInCate(@PathVariable("id") Integer id, @RequestBody List<Integer> subIds) {
        Category category = categoryService.findById(id).get();
        Set<SubCategory> subCategories = category.getSubCategories().stream().filter(i -> !subIds.contains(i.getId())).collect(Collectors.toSet());

        category.setSubCategories(subCategories);
        categoryService.save(category);
        return ResponseEntity.ok("Remove subcategory successfully!");
    }

}
