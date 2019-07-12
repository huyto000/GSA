package com.example.GemSkillAssessment.controller.user;

import com.example.GemSkillAssessment.config.security.JwtTokenProvider;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.error.NotAcceptableException;
import com.example.GemSkillAssessment.model.JWT;
import com.example.GemSkillAssessment.model.User;
import com.example.GemSkillAssessment.model.dto.DataDTO;
import com.example.GemSkillAssessment.model.dto.PasswordDto;
import com.example.GemSkillAssessment.model.dto.UserDto;
import com.example.GemSkillAssessment.service.userservice.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping(value = "/userAuthentication")
public class UserAuthController {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/inviteListUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> inviteListUser(@Valid @RequestBody() UserDto userDto) {
        List<String> ids = userDto.getIdEmployees();
        userService.inviteListUser(ids);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }


    @PostMapping("/reviewSelf/{id}")
    public ResponseEntity<?> reviewSelf(@PathVariable("id") Integer id) {
        userService.remindUser(id);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<?> review(@PathVariable("id") Integer id) {
        userService.remindReviewUser(id);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @PutMapping("/deactiveUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactiveUser(@Valid @RequestBody() UserDto userDto) {
        for (String id : userDto.getIdEmployees()) {
            userService.deactiveUser(id);
        }
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @PutMapping("/authEdit")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> authEdit(@Valid @RequestBody() UserDto userDto) {
        for (String id : userDto.getIdEmployees()) {
            userService.authEdit(id);
        }
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @PostMapping("/changePassword")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> changePassword(@Valid @RequestBody() PasswordDto passwordDto) {
        User user = userService.getUserAth();

        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmonPassword()))
            throw new NotAcceptableException(EError.CONFIRM.getLabel());
        if (!userService.checkPassword(user.getPassword(), passwordDto.getOldPassword()))
            throw new NotAcceptableException(EError.OLD_PASSWORD_WRONG.getLabel());

        userService.updatePassword(user, passwordDto.getNewPassword());
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @PostMapping("/logout")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> logout(@Valid @RequestBody() String jwt) {
        Date dateUpdate = new Date();
        JWT jwtLock1 = new JWT();
        jwtLock1.setCode(jwt);
        jwtLock1.setLockJwt(true);
        jwtLock1.setDateUpdate(dateUpdate);
        userService.saveJwt(jwtLock1);
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }
}
