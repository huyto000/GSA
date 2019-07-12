package com.example.GemSkillAssessment.controller.user;

import com.example.GemSkillAssessment.dao.PermissionRepository;
import com.example.GemSkillAssessment.dao.UserRepository;
import com.example.GemSkillAssessment.enumerted.EJobTitle;
import com.example.GemSkillAssessment.enumerted.EPermission;
import com.example.GemSkillAssessment.error.NotFoundException;
import com.example.GemSkillAssessment.model.User;
import com.example.GemSkillAssessment.model.dto.DataDTO;
import com.example.GemSkillAssessment.model.dto.EmployeeDTO;
import com.example.GemSkillAssessment.service.permission.PermissionService;
import com.example.GemSkillAssessment.service.userservice.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin()
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    PermissionRepository permissionRepository;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> listUser() {
        List<User> users = (List<User>) userRepository.findAll();
        users = users.stream().filter(item -> !"".equals(item.getIdEmployee()) && !EJobTitle.ADMIN.equals(item.getVaEJobTitle())).collect(Collectors.toList());
        for (User user : users) {
            if (userService.getSetSuperviserId(users).contains(user.getId())) {
                user.getPermissions().add(permissionRepository.findByType(EPermission.REVIEW));
            } else {
                if (user.getPermissions().contains(permissionRepository.findByType(EPermission.REVIEW)))
                    user.getPermissions().remove(permissionRepository.findByType(EPermission.REVIEW));
            }
        }
        ModelMapper modelMapper = new ModelMapper();
        List<EmployeeDTO> employeeDTOs = users.stream().map(i -> modelMapper.map(i, EmployeeDTO.class)).collect(Collectors.toList());
        userService.setPermission(employeeDTOs);
        return new ResponseEntity<List<EmployeeDTO>>(employeeDTOs, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getUser(@PathVariable("id") int id) {

        User user = userRepository.findById(id);
        if (user == null) throw new NotFoundException("User not found");
        ModelMapper modelMapper = new ModelMapper();
        EmployeeDTO employeeDTO = modelMapper.map(user, EmployeeDTO.class);
        userService.setPermission(employeeDTO);
        return new ResponseEntity<EmployeeDTO>(employeeDTO, HttpStatus.OK);
    }

    @GetMapping("/users/email")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getUserByEmail(@RequestParam("email") String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not found");
        ModelMapper modelMapper = new ModelMapper();
        EmployeeDTO employeeDTO = modelMapper.map(user, EmployeeDTO.class);
        userService.setPermission(employeeDTO);
        return new ResponseEntity<EmployeeDTO>(employeeDTO, HttpStatus.OK);
    }

    @GetMapping("/users/{id}/supervised")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getListEmployeeBySuperviserId(@PathVariable("id") int id) {
        List users1 = (List) userRepository.findAll();
        List<User> users = userService.getListEmployeeFromSupervisedId(id, users1);
        users = users.stream().filter(item -> !"".equals(item.getIdEmployee()) && !"admin@gmail.com".equals(item.getEmail())).collect(Collectors.toList());
        ModelMapper modelMapper = new ModelMapper();
        List<EmployeeDTO> employeeDTOs = users.stream().map(i -> modelMapper.map(i, EmployeeDTO.class)).collect(Collectors.toList());
        userService.setPermission(employeeDTOs);
        return new ResponseEntity<List<EmployeeDTO>>(employeeDTOs, HttpStatus.OK);
    }

    @GetMapping("/users/supervised")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getListEmployeeBySuperviserEmail(@RequestParam("email") String email) {
        ModelMapper modelMapper = new ModelMapper();
        List<User> users = userService.getListEmployeeFromSupervisedEmail(email);
        List<EmployeeDTO> employeeDTOs = users.stream().map(i -> modelMapper.map(i, EmployeeDTO.class)).collect(Collectors.toList());
        userService.setPermission(employeeDTOs);
        return new ResponseEntity<List<EmployeeDTO>>(employeeDTOs, HttpStatus.OK);
    }

    @PutMapping("/users/{id}/supervised/{idSupervisedBy}")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateSuperviser(@PathVariable("id") int id, @PathVariable("idSupervisedBy") int idSuperviser) {
        userService.updateSuperviser(id, idSuperviser);
        return ResponseEntity.ok("Done");
    }

    @GetMapping("/getJobtitle")
    public ResponseEntity<?> getJobtitle() {
        return ResponseEntity.ok(new DataDTO(EJobTitle.values(), "Successfully"));
    }

    @PutMapping("/users/{id}/jobtitle")
    public ResponseEntity<?> updateJobtitle(@PathVariable("id") int id, @RequestParam("eJobTitle") String eJobTitle) {
        userService.updateJobtitle(id, eJobTitle);
        return ResponseEntity.ok("Successfull");
    }

    @PutMapping("/users/{id}/left")
    public ResponseEntity<?> changeLeft(@PathVariable("id") int id) {
        userService.setLeft(id);
        return ResponseEntity.ok("Successfull");
    }

//    @PutMapping("/users/{userId}/lockEdit")
//    public ResponseEntity<?> lockEdit(@PathVariable("userId") int userId){
//        permissionService.lockEdit(userId);
//        return ResponseEntity.ok("Lock Successfull");
//    }
//    @PutMapping("/users/{userId}/lockFill")
//    public ResponseEntity<?> lockFill(@PathVariable("userId") int userId){
//        permissionService.lockFill(userId);
//        return ResponseEntity.ok(" Lock Successfull");
//    }
}
