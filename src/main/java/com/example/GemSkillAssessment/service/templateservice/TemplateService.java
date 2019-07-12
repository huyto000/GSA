package com.example.GemSkillAssessment.service.templateservice;

import com.example.GemSkillAssessment.dao.CategoryRepository;
import com.example.GemSkillAssessment.dao.PeriodRepository;
import com.example.GemSkillAssessment.dao.SubCategoryRepository;
import com.example.GemSkillAssessment.dao.TemplateRepository;
import com.example.GemSkillAssessment.enumerted.EJobTitle;
import com.example.GemSkillAssessment.error.NotAcceptableException;
import com.example.GemSkillAssessment.model.BaseItem;
import com.example.GemSkillAssessment.model.Category;
import com.example.GemSkillAssessment.model.SubCategory;
import com.example.GemSkillAssessment.model.Template;
import com.example.GemSkillAssessment.model.dto.TemplatePeriodDTO;
import com.example.GemSkillAssessment.service.period.PeriodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;
@Transactional
@Service
public class TemplateService {
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired

    private PeriodService periodService;
    @Autowired
    private FormService formService;
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public Set<Template> findAll() {
        return templateRepository.findAll();
    }

   /* public Set<Template> findAllByPeriodId(Integer id) {
        return templateRepository.findAllByPeriodId(id);
    }*/

    public Template findById(Integer id) {
        return templateRepository.findById(id);
    }

    public void deleteById(Integer id) {
        Template template = templateRepository.findById(id);
        if (template.getCategories().size() > 0) throw new NotAcceptableException("Template has data! Can't delete !");
        if (periodRepository.findByEnabledTrue() != null)
            throw new NotAcceptableException("Period is enable! Can't delete !");
        templateRepository.deleteById(id);
    }


    public boolean checkPositionInListTemplate(Template templateInsert, Set<Template> templates) {
        if (templateInsert.getEJobTitle() == null) {
            System.out.println("Position is null");
            return true;
        }
        for (Template template : templates) {
            if (templateInsert.getEJobTitle() == template.getEJobTitle()) {
                return true;
            }
        }
        return false;
    }

    public void save(Template template) {
        try {
            templateRepository.save(template);
            formService.createdForm(periodRepository.findByEnabledTrue());
        } catch (IllegalArgumentException n) {
            throw new NotAcceptableException("JobTitle can't be null or wrong format");
        }

    }

    public Template findByEJobTitle(EJobTitle eJobTitle) {
        for (Template template : templateRepository.findAll()) {
            if (template.getEJobTitle() == eJobTitle) return template;
        }
        return null;
    }

    public Template findByEJobTitleAndPeriod(Set<Template> templates, EJobTitle eJobTitle, Integer idPeriod) {
        if (templates == null || eJobTitle== null || idPeriod == null)
            return null;
        else
            return templates.stream().filter(template -> template.getEJobTitle() == eJobTitle).findFirst().orElse(null);
//         && template.getPeriod().getId() == idPeriod
    }

   /* public Template getByPeriodFormEJobTitle(Set<Template> templates, Integer idPeriod) {
        return templates.stream().filter(template -> template.getPeriod().getId() == idPeriod).findFirst().get();
    }*/

    public Set<SubCategory> getListSubFromCateId(Integer cateId) {
        Optional<Category> category = categoryRepository.findById(cateId);
        return category.get().getSubCategories();
    }

    public boolean checkDuplicateBaseInTemp(Template template) {
        List<Integer> setBaseId = new ArrayList<>();
        for (Category category : template.getCategories()) {
            System.out.println(category.getId());
            for (SubCategory subCategory : this.getListSubFromCateId(category.getId())) {
                /*if(setSubId.contains(subCategory.getId())) return true;
                setSubId.add(subCategory.getId());*/
                for (BaseItem baseItem : subCategory.getBaseItems()) {
                    if (setBaseId.contains(baseItem.getId())) return true;
                    setBaseId.add(baseItem.getId());
                }
            }
        }
        System.out.println(setBaseId);
        return false;
    }
    public List<Integer> getListIdCateOfTemp(Integer tempId){
        Template template = templateRepository.findById(tempId);
        List<Integer> listId = new ArrayList<>();
        for(Category category : template.getCategories()){
            listId.add(category.getId());
        }
        return listId;
    }
    public List<Integer> getListSubOfCate(Integer tempId){
        Optional<Category> category = categoryRepository.findById(tempId);
        List<Integer> listId = new ArrayList<>();
        for(SubCategory subCategory : category.get().getSubCategories()){
            listId.add(subCategory.getId());
        }
        return listId;
    }
    public List<Integer> getListBaseOfSub(Integer tempId){
        SubCategory subCategory = subCategoryRepository.findById(tempId);
        List<Integer> listId = new ArrayList<>();
        for(BaseItem baseItem : subCategory.getBaseItems()){
            listId.add(baseItem.getId());
        }
        return listId;
    }

    public void cloneTemplateLastPeriod(){

    }
    public void writeTemplateToFile(Set<TemplatePeriodDTO> templatePeriodDTOs){
        try {

            FileOutputStream fos = new FileOutputStream("TemplateStorage.txt");
            ObjectOutputStream obos = new ObjectOutputStream(fos);
            obos.writeObject(templatePeriodDTOs);

            fos.close();
            obos.flush();

            System.out.println("Write template to file Success");
            obos.close();


        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public boolean checkEmptyFile() {
        File newFile = new File("TemplateStorage.txt");
        return newFile.length() == 0;
    }

    public Set<TemplatePeriodDTO> readFile() throws FileNotFoundException, IOException, ClassNotFoundException {
        Set<TemplatePeriodDTO> lsttemp = new HashSet<>();

        if(this.checkEmptyFile()) return new HashSet<>();
        try {
            ObjectInputStream obis = new ObjectInputStream(new FileInputStream("TemplateStorage.txt"));
            Object object = obis.readObject();
            lsttemp = (Set<TemplatePeriodDTO>)object;
            obis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lsttemp;
    }

    public Set<Template> getListTemplateByPeriodId(Integer idPeriod) throws IOException, ClassNotFoundException {
        Set<TemplatePeriodDTO> templatePeriodDTOS = this.readFile();
        for(TemplatePeriodDTO templatePeriodDTO : templatePeriodDTOS){
            if(templatePeriodDTO.getPeriodId()==idPeriod) return templatePeriodDTO.getTemplates();
        }
        return new HashSet<>();
    }

}
