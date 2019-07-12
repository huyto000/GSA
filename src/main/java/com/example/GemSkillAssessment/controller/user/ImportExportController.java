package com.example.GemSkillAssessment.controller.user;

import com.example.GemSkillAssessment.dao.PermissionRepository;
import com.example.GemSkillAssessment.dao.UserRepository;
import com.example.GemSkillAssessment.error.NotAcceptableException;
import com.example.GemSkillAssessment.model.Period;
import com.example.GemSkillAssessment.model.User;
import com.example.GemSkillAssessment.model.dto.DataDTO;
import com.example.GemSkillAssessment.service.manage.ExcelService;
import com.example.GemSkillAssessment.service.period.PeriodService;
import com.example.GemSkillAssessment.service.templateservice.FormService;
import com.example.GemSkillAssessment.service.userservice.IUserService;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@CrossOrigin()
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class ImportExportController {
    @Autowired
    private ExcelService excelService;
    @Autowired
    private IUserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private FormService formService;
    @Autowired
    private PeriodService periodService;

    @RequestMapping(value = "/import-user", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Object> importUser(@RequestParam("file") MultipartFile excelFile) throws IOException, FileNotFoundException, ParseException {
        try {
            byte[] data = excelFile.getBytes();
            List<User> users = userService.importUser(data);
        } catch (NotOfficeXmlFileException n) {
            throw new NotAcceptableException("Wrong format file");
        }
        userService.importReview();
        Period period = periodService.findByEnabledTrue();
        formService.createdForm(period);
        return new ResponseEntity<>("Import Successfully", HttpStatus.OK);
    }

    @PostMapping("/import-category")
    public ResponseEntity importCategory(@RequestParam("file") MultipartFile multipartFile) {
        excelService.importCategory(multipartFile);
        return ResponseEntity.ok(new DataDTO("Successfully!"));
    }

    @PostMapping("/import-review")
    public ResponseEntity importReview() {
        userService.importReview();
        return ResponseEntity.ok(new DataDTO("Successfully!"));
    }

//    @PostMapping("/exportReport/{idPeriod}")
//    public ResponseEntity<InputStreamResource> exportReport(@PathVariable("idPeriod") Integer idPeriod){
//        HttpHeaders responseHeader = new HttpHeaders();
//        responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        responseHeader.set("Content-disposition", "attachment; filename=User.xlsx");
//
//        InputStreamResource inputStreamResource = excelService.exportReport(idPeriod);
//        return new ResponseEntity<InputStreamResource>(inputStreamResource,responseHeader, HttpStatus.OK);
//    }
}

