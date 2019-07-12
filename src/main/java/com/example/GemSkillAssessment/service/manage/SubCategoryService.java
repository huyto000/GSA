package com.example.GemSkillAssessment.service.manage;

import com.example.GemSkillAssessment.dao.BaseItemRepository;
import com.example.GemSkillAssessment.dao.SubCategoryRepository;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.error.NotAcceptableException;
import com.example.GemSkillAssessment.error.NotFoundException;
import com.example.GemSkillAssessment.model.BaseItem;
import com.example.GemSkillAssessment.model.SubCategory;
import com.example.GemSkillAssessment.model.dto.SubCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private BaseItemRepository baseItemRepository;

    public void save(SubCategoryDTO subCategoryDTO) {
        SubCategory subCategory = new SubCategory();
        Set<BaseItem> baseItems;
        try {
            if (subCategoryDTO.getId() != null)
                subCategory = subCategoryRepository.findById(subCategoryDTO.getId());
            else if (subCategoryDTO.getName() == null)
                throw new NotFoundException(EError.SUB_IS_NULL.getLabel());
            else if (subCategoryRepository.findByName(subCategoryDTO.getName()) != null)
                throw new NotFoundException(EError.SUB_IS_EXIST.getLabel());

            if (subCategoryDTO.getName() != null) {
                if (subCategoryRepository.findByName(subCategoryDTO.getName()) != null)
                    throw new NotFoundException(EError.SUB_IS_EXIST.getLabel());
                subCategory.setName(subCategoryDTO.getName());
            }
        } catch (NullPointerException e) {
            throw new NotFoundException(EError.ID_NOT_FOUND.getLabel());
        }

        if (subCategoryDTO.getIdBaseItems() != null) {
            if (subCategoryDTO.getIdBaseItems().size() == 0)
                baseItems = new HashSet<>();
            else {
                baseItems = baseItemRepository.findAllByIdIn(subCategoryDTO.getIdBaseItems());
                if (baseItems.size() == 0) throw new NotFoundException(EError.BASE_NOT_EXITS.getLabel());
            }
            subCategory.setBaseItems(baseItems);
        }
        subCategoryRepository.save(subCategory);
    }

    public void delete(List<Integer> ids) {
        for (Integer id : ids) {
            SubCategory subCategory = subCategoryRepository.findById(id);
            if (subCategory.getBaseItems().size() > 0)
                throw new NotAcceptableException(EError.SUBCATEGORY_NOT_DELETE.getLabel());
            subCategoryRepository.deleteById(id);
        }
    }

    public Set<BaseItem> checkOutSub(Integer idSub) {
        SubCategory subCategory = new SubCategory();
        try {
            subCategory = subCategoryRepository.findById(idSub);
        } catch (NullPointerException e) {
            throw new NotFoundException(EError.ID_NOT_FOUND.getLabel());
        }
        Set<BaseItem> baseItems = baseItemRepository.findAll();
        baseItems.removeAll(subCategory.getBaseItems());
        return baseItems;
    }

    public Set<SubCategory> findAll() {
        return subCategoryRepository.findAll();
    }

    public Set<SubCategory> findSubCategoriesNotInCategory(Integer id) {
        return subCategoryRepository.findSubCategoriesNotInCategory(id);
    }

    public SubCategory findById(Integer id) {
        return subCategoryRepository.findById(id);
    }
}
