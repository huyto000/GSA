package com.example.GemSkillAssessment.controller.template;

import com.example.GemSkillAssessment.dao.PeriodRepository;
import com.example.GemSkillAssessment.dao.TemplateRepository;
import com.example.GemSkillAssessment.enumerted.EJobTitle;
import com.example.GemSkillAssessment.error.NotAcceptableException;
import com.example.GemSkillAssessment.model.Category;
import com.example.GemSkillAssessment.model.SubCategory;
import com.example.GemSkillAssessment.model.Template;
import com.example.GemSkillAssessment.model.dto.TemplatePeriodDTO;
import com.example.GemSkillAssessment.service.manage.CategoryService;
import com.example.GemSkillAssessment.service.period.PeriodService;
import com.example.GemSkillAssessment.service.templateservice.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin()
@RestController
@RequestMapping("/templates")
@PreAuthorize("hasRole('ADMIN')")
public class TemplateController {
    @Autowired
    private TemplateService templateService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private PeriodService periodService;
//
//   @GetMapping("")
//    public ResponseEntity<Set<Template>> getAll(){
//        Set<Template> templates = templateService.findAll();
//        return ResponseEntity.ok(templates);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Template> getById(@PathVariable("id") int id) {
        Template template = new Template();
        try {
            template = templateService.findById(id);
            System.out.println(templateService.checkDuplicateBaseInTemp(template));

        } catch (NoSuchElementException n) {
            throw new NotAcceptableException("Id not exist");
        }
        return ResponseEntity.ok(template);
    }

    @GetMapping("")
    public ResponseEntity<?> getByJobtitle(@RequestParam(value = "eJobTitle", required = false) String eJobTitle) {
        if (eJobTitle == null) {
            Set<Template> templates = templateRepository.findAll();
                return ResponseEntity.ok(templates);
            }

        Template template = new Template();
        try {
            template = templateService.findByEJobTitle(EJobTitle.valueOf(eJobTitle));

        } catch (IllegalArgumentException n) {
            throw new NotAcceptableException("JobTitle can't be null or wrong format");
        }
        return ResponseEntity.ok(template);
    }

    @PostMapping("")
    public ResponseEntity<?> createTemplate(@RequestBody() Template template) {
        if (periodRepository.findByEnabledTrue() != null)
            throw new NotAcceptableException("Period is enable, can't create template");
        Set<Template> templates = templateRepository.findAll();
        try {
            if (!templateService.checkPositionInListTemplate(template, templates)) {
                if (templateService.checkDuplicateBaseInTemp(template))
                    throw new NotAcceptableException("Duplicate BaseItem in Template, can't add Category");
                templateService.save(template);
            } else throw new NotAcceptableException("Position exist! Please input another position");

        } catch (IllegalArgumentException n) {
            throw new NotAcceptableException("JobTitle can't be null or wrong format");
        }
        return ResponseEntity.ok("Create successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemplate(@PathVariable("id") int id, @RequestBody() Template template) {
        if (periodRepository.findByEnabledTrue() != null)
            throw new NotAcceptableException("Period is enable, can't update");
        Template templateUpdate = templateService.findById(id);
        Set<Template> templates = templateRepository.findAll();
        Set<Integer> setSubId = new HashSet<>();
        if (templateService.checkDuplicateBaseInTemp(template))
            throw new NotAcceptableException("Duplicate BaseItem in Template, can't add Category");
        /*for(Category category : template.getCategories()){
            for(SubCategory subCategory : category.getSubCategories()){
                if(setSubId.contains(subCategory.getId())) throw new NotAcceptableException("Duplicate Subcategory in Template, can't add Category");
                setSubId.add(subCategory.getId());
            }
        }*/
        if (!templateService.checkPositionInListTemplate(template, templates) || templateRepository.findById(id).getEJobTitle() == template.getEJobTitle()) {
            templateUpdate.setEJobTitle(template.getEJobTitle());
            templateUpdate.setCategories(template.getCategories());
            templateService.save(templateUpdate);
        } else throw new NotAcceptableException("Position exist! Please input another position");


        return ResponseEntity.ok("Update successfully");
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemplate(@PathVariable("id") Integer id) {
        templateService.deleteById(id);
        return ResponseEntity.ok("Delete successfully");
    }

    @PostMapping("/{id}/categories")
    public ResponseEntity<?> addCategoryToTemplate(@PathVariable("id") int id, @RequestParam("cateId") int cateId) {
        if (periodRepository.findByEnabledTrue() != null)
            throw new NotAcceptableException("Period is enable, can't update");
        Template template = templateService.findById(id);
        Category categoryAdd = categoryService.findById(cateId).get();
        Set<Integer> setSubId = new HashSet<>();
        for (Category category : template.getCategories()) {
            for (SubCategory subCategory : category.getSubCategories()) {
                setSubId.add(subCategory.getId());
            }
        }

        for (SubCategory subCategory : categoryAdd.getSubCategories()) {
            if (setSubId.contains(subCategory.getId()))
                throw new NotAcceptableException("Duplicate BaseItem in Template, can't add Category");
        }
        template.getCategories().add(categoryAdd);
        templateService.save(template);
        return ResponseEntity.ok("Add template successfully");
    }

    @DeleteMapping("/{id}/categories")
    public ResponseEntity<?> deleteCategoryFromTemplate(@PathVariable("id") Integer id, @RequestParam("cateId") int cateId) {

        if (periodRepository.findByEnabledTrue() != null)
            throw new NotAcceptableException("Period is enable, can't update");
        try {
            Template template = templateService.findById(id);
            Set<Category> categories = template.getCategories().stream().filter(i -> !(cateId == i.getId())).collect(Collectors.toSet());
            template.setCategories(categories);
            templateService.save(template);
        } catch (NoSuchElementException e) {
            throw new NotAcceptableException("Id template not found");
        }
        return ResponseEntity.ok("Remove category successfully!");
    }

    @GetMapping("/showtemp")
    public ResponseEntity<?> getById() throws IOException, ClassNotFoundException {
        Set<TemplatePeriodDTO> templatePeriodDTOS = templateService.readFile();
        return ResponseEntity.ok("Show successfully");
    }
    @Transactional
    @GetMapping("/{idPeriod}/period")
    public ResponseEntity<?> getListTempByPeriodId(@PathVariable("idPeriod") Integer idPeriod) throws IOException, ClassNotFoundException {
        Set<Template> templates = templateService.getListTemplateByPeriodId(idPeriod);
        for(Template template : templates){
            for(Category category : template.getCategories()){
                System.out.println(category.getName());
            }
        }
        return ResponseEntity.ok(templates);
    }



}
