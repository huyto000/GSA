package com.example.GemSkillAssessment.controller.template;

import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.enumerted.EKeyFunction;
import com.example.GemSkillAssessment.model.Period;
import com.example.GemSkillAssessment.model.User;
import com.example.GemSkillAssessment.model.dto.DataDTO;
import com.example.GemSkillAssessment.model.dto.DataListDTO;
import com.example.GemSkillAssessment.model.dto.FormBaseItemDTO;
import com.example.GemSkillAssessment.model.dto.SumPointCategoryDTO;
import com.example.GemSkillAssessment.service.manage.ExcelService;
import com.example.GemSkillAssessment.service.period.PeriodService;
import com.example.GemSkillAssessment.service.templateservice.FormService;
import com.example.GemSkillAssessment.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/form")
@PreAuthorize("hasRole('MEMBER')")
public class FormController {
    @Autowired
    private FormService formService;
    @Autowired
    private UserService userService;
    @Autowired
    private PeriodService periodService;
    @Autowired
    private ExcelService excelService;

    @PostMapping("")
    public ResponseEntity<?> saveForm(@RequestBody List<FormBaseItemDTO> formBaseItemDTOs) {
        User user = userService.getUserAth();
        formService.save(formBaseItemDTOs, user, EKeyFunction.FILL_EDIT);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @PostMapping("/finish/{id}")
    public ResponseEntity<?> finishForm(@PathVariable("id") Integer id) {
        User user = userService.getUserAth();
        if (user.getId() != id) {
            User user1 = userService.findByIdAndSupervisedId(id, user.getId());
            formService.checkFinish(user1, EKeyFunction.REVIEW);
            return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
        } else {
            formService.checkFinish(user, EKeyFunction.FILL_EDIT);
            return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
        }
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<?> review(@PathVariable("id") Integer id, @RequestBody List<FormBaseItemDTO> formBaseItemDTOs) {
        User user = userService.getUserAth();
        User user1 = userService.findByIdAndSupervisedId(id, user.getId());
        formService.save(formBaseItemDTOs, user1, EKeyFunction.REVIEW);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @GetMapping("/{userId}/{idPeriod}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<?> getFormByUser(@PathVariable("userId") Integer userId, @PathVariable("idPeriod") Integer idPeriod) {
        return this.functionGetForm(EKeyFunction.GET_POINT_OF_FORM, userId, idPeriod);
    }

    @GetMapping("/sumCate/{userId}/{idPeriod}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<?> getCateSubByUser(@PathVariable("userId") Integer userId, @PathVariable("idPeriod") Integer idPeriod) {
        return this.functionGetForm(EKeyFunction.GET_SUM_POINT_CATE, userId, idPeriod);
    }

    @GetMapping("report/{idPeriod}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCate(@PathVariable("idPeriod") Integer idPeriod) {
        return ResponseEntity.ok(new DataListDTO<>(EError.SUCCESSFULLY.getLabel(), formService.findAllCate(idPeriod, EKeyFunction.VIEW_REPORT)));
    }

    @GetMapping("sumPointCategory/{idCate}/{idPeriod}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> sumPointCate(@PathVariable("idPeriod") Integer idPeriod, @PathVariable("idCate") Integer idCate) {
        return ResponseEntity.ok(new DataDTO<SumPointCategoryDTO>(formService.sumPointCate(idPeriod, idCate), EError.SUCCESSFULLY.getLabel()));
    }

    @GetMapping("/exportReport/{idPeriod}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamResource> exportReport(@PathVariable("idPeriod") Integer idPeriod) {
        HttpHeaders responseHeader = new HttpHeaders();
        Period period = periodService.checkPeriodStart(idPeriod);
        String nameFile = period.getName() + ".xlsx";
        responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeader.set("Content-disposition", "attachment; filename=" + nameFile);
        InputStreamResource inputStreamResource = excelService.exportReport(idPeriod);
        return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
    }

    @GetMapping("/exportForm/{userId}/{idPeriod}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamResource> exportReport(@PathVariable("userId") Integer userId, @PathVariable("idPeriod") Integer idPeriod) throws UnsupportedEncodingException {
        User user = userService.findById(userId);
        HttpHeaders responseHeader = new HttpHeaders();
        String nameFile = user.getName() + ".xlsx";
        nameFile = URLEncoder.encode(nameFile, "UTF-8");
        responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeader.set("Content-disposition", "attachment; filename=" + nameFile);
        InputStreamResource inputStreamResource = excelService.exportFormUser(idPeriod, user);
        return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
    }

    private ResponseEntity<?> functionGetForm(EKeyFunction eKeyFunction, Integer userId, Integer idPeriod) {
        User user = userService.getUserAth();
        Period period = periodService.findByEnabledTrue();
        if (user.getId() != userId) {
            if (user.isAdmin()) {
                period = periodService.checkPeriodStart(idPeriod);
                return ResponseEntity.ok(new DataDTO<>(formService.findFormByUser(period, userService.findById(userId), eKeyFunction, false), EError.SUCCESSFULLY.getLabel()));
            } else {
                User user1 = userService.findByIdAndSupervisedId(userId, user.getId());
                return ResponseEntity.ok(new DataDTO<>(formService.findFormByUser(period, user1, eKeyFunction, false), EError.SUCCESSFULLY.getLabel()));
            }
        } else
            return ResponseEntity.ok(new DataDTO<>(formService.findFormByUser(period, user, eKeyFunction, true), EError.SUCCESSFULLY.getLabel()));
    }
}

