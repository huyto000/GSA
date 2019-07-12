package com.example.GemSkillAssessment.controller.period;

import com.example.GemSkillAssessment.dao.PeriodRepository;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.model.Period;
import com.example.GemSkillAssessment.model.dto.DataDTO;
import com.example.GemSkillAssessment.service.period.PeriodService;
import com.example.GemSkillAssessment.service.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/period")
@PreAuthorize("hasRole('ADMIN')")
public class PeriodController {
    @Autowired
    private PeriodService periodService;
    @Autowired
    private PeriodRepository periodRepository;
    @Autowired
    private PermissionService permissionService;

    @PostMapping("")
    public ResponseEntity<?> savePeriod(@RequestBody() String name) {
        Period period = new Period();
        period.setEnabled(true);
        period.setName(name);
        period.setStartDate(new Date());
        periodService.addPeriod(period);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @PostMapping("/finish")
    public ResponseEntity<?> finishPeriod(@RequestBody() Integer id) throws IOException, ClassNotFoundException {
        periodService.finishPeriod(id);
        permissionService.openFillAll();
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }



    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<Period> periodList = (List<Period>) periodRepository.findAll();
        return ResponseEntity.ok(new DataDTO(periodList, EError.SUCCESSFULLY.getLabel()));
    }

    @PutMapping("/{periodId}")
    public ResponseEntity<?> updatePeriod(@PathVariable("periodId") Integer periodId, @RequestBody Period period) {
        periodService.updatePeriod(periodId, period);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @DeleteMapping("/{periodId}")
    public ResponseEntity<?> deletePeriod(@PathVariable("periodId") Integer periodId) {
        periodService.deletePeriod(periodId);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    /*@PutMapping("/{periodId}/status")
    public ResponseEntity<?> updatePeriodStatus(@PathVariable("periodId") Integer periodId) {

        periodService.updateStatusPeriod(periodId);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }*/
}
