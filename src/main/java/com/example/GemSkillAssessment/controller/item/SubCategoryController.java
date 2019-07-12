package com.example.GemSkillAssessment.controller.item;

import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.error.NotFoundException;
import com.example.GemSkillAssessment.model.BaseItem;
import com.example.GemSkillAssessment.model.SubCategory;
import com.example.GemSkillAssessment.model.dto.DataDTO;
import com.example.GemSkillAssessment.model.dto.DataListDTO;
import com.example.GemSkillAssessment.model.dto.SubCategoryDTO;
import com.example.GemSkillAssessment.service.manage.CategoryService;
import com.example.GemSkillAssessment.service.manage.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@CrossOrigin()
@RestController
@RequestMapping("/sub")
@PreAuthorize("hasRole('ADMIN')")
public class SubCategoryController {
    @Autowired
    private SubCategoryService subCategoryService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        Set<SubCategory> subCategories = subCategoryService.findAll();
        return ResponseEntity.ok(new DataListDTO<SubCategory>(subCategories, EError.SUCCESSFULLY.getLabel()));
    }

    @PostMapping("/save")
    public ResponseEntity<?> add(@RequestBody() SubCategoryDTO subCategoryDTO, HttpServletRequest request) {
        subCategoryService.save(subCategoryDTO);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody() List<Integer> ids, HttpServletRequest request) {
        try {
            subCategoryService.delete(ids);
        } catch (Exception e) {
            throw new NotFoundException(EError.SUBCATEGORY_NOT_DELETE.getLabel());
        }
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @GetMapping("/{id}/getBaseItemOutSub")
    public ResponseEntity<?> getOutSub(@PathVariable("id") Integer id, HttpServletRequest request) {
        return ResponseEntity.ok(new DataListDTO<BaseItem>(subCategoryService.checkOutSub(id), "Done"));
    }

    @GetMapping("/notInCategory")
    public ResponseEntity<Set<SubCategory>> getSubByNotInCatId(@RequestParam("id") Integer id) {

        Set<SubCategory> subCategories = subCategoryService.findSubCategoriesNotInCategory(id);

        return ResponseEntity.ok(subCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id, HttpServletRequest request) {
        return ResponseEntity.ok(new DataDTO<SubCategory>(subCategoryService.findById(id), "Done"));
    }


}
