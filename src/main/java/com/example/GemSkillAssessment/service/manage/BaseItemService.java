package com.example.GemSkillAssessment.service.manage;

import com.example.GemSkillAssessment.dao.BaseItemRepository;
import com.example.GemSkillAssessment.enumerted.EBaseItemType;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.error.NotFoundException;
import com.example.GemSkillAssessment.model.BaseItem;
import com.example.GemSkillAssessment.model.dto.BaseItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class BaseItemService {
    @Autowired
    private BaseItemRepository baseItemRepository;

    public void save(BaseItemDTO baseItemDTO, EBaseItemType eBaseItemType) {
        BaseItem baseItem = new BaseItem();
        try {
            if (baseItemDTO.getId() != null)
                baseItem = baseItemRepository.findById(baseItemDTO.getId());
            else if (baseItemDTO.getName() == null)
                throw new NotFoundException(EError.BASE_IS_NULL.getLabel());
            else if (baseItemRepository.findByNameAndType(baseItemDTO.getName(), eBaseItemType) != null)
                throw new NotFoundException(EError.BASE_NOT_EXITS.getLabel());
        } catch (NullPointerException e) {
            throw new NotFoundException(EError.ID_NOT_FOUND.getLabel());
        }
        if (baseItemDTO.getName() != null) baseItem.setName(baseItemDTO.getName());
        if (baseItemDTO.getType() != null) baseItem.setType(eBaseItemType);
        baseItemRepository.save(baseItem);
    }

    public void delete(List<Integer> ids) {
        for (Integer id : ids) {
            baseItemRepository.deleteById(id);
        }
    }

    public Set<BaseItem> findAll() {
        return baseItemRepository.findAllByOrderById();
    }

    public Set<BaseItem> findByType(String type) {
        return baseItemRepository.findByType(this.checkBaseType(type));
    }

    public EBaseItemType checkBaseType(String type) {
        for (EBaseItemType eBaseItemType : EBaseItemType.values()) {
            if (eBaseItemType.name().equals(type)) {
                return eBaseItemType;
            }
        }
        throw new NotFoundException(EError.BASEITEM_TYPE_NOT_FOUND.getLabel());
    }
}