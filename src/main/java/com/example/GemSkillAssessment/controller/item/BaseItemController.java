package com.example.GemSkillAssessment.controller.item;

import com.example.GemSkillAssessment.enumerted.EBaseItemType;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.error.NotImplementedException;
import com.example.GemSkillAssessment.model.BaseItem;
import com.example.GemSkillAssessment.model.dto.BaseItemDTO;
import com.example.GemSkillAssessment.model.dto.DataDTO;
import com.example.GemSkillAssessment.model.dto.DataListDTO;
import com.example.GemSkillAssessment.service.manage.BaseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@CrossOrigin()
@RestController
@RequestMapping("/baseItem")
@PreAuthorize("hasRole('ADMIN')")
public class BaseItemController {
    @Autowired
    private BaseItemService baseItemService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        Set<BaseItem> baseItemList = baseItemService.findAll();
        return ResponseEntity.ok(new DataListDTO<BaseItem>(baseItemList, EError.SUCCESSFULLY.getLabel()));
    }

    @GetMapping("/getByType")
    public ResponseEntity<?> getByType(@RequestParam("type") String type, HttpServletRequest request) {
        return ResponseEntity.ok(new DataListDTO<BaseItem>(baseItemService.findByType(type), EError.SUCCESSFULLY.getLabel()));
    }

    @PostMapping("/save")
    public ResponseEntity<?> add(@RequestBody() BaseItemDTO baseItemDTO, HttpServletRequest request) {
        if (baseItemDTO.getType() == null && baseItemDTO.getId() != null) {
            baseItemService.save(baseItemDTO, null);
        } else {
            EBaseItemType eBaseItemType = baseItemService.checkBaseType(baseItemDTO.getType());
            baseItemService.save(baseItemDTO, eBaseItemType);
        }
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody() List<Integer> ids, HttpServletRequest request) {
        try {
            baseItemService.delete(ids);
        } catch (Exception e) {
            throw new NotImplementedException(EError.BASEITEM_NOT_DELETE.getLabel());
        }
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

}
