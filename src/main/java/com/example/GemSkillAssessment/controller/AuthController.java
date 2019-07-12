package com.example.GemSkillAssessment.controller;

import com.example.GemSkillAssessment.config.security.JwtAuthenticationResponse;
import com.example.GemSkillAssessment.config.security.JwtTokenProvider;
import com.example.GemSkillAssessment.enumerted.EError;
import com.example.GemSkillAssessment.enumerted.EPermission;
import com.example.GemSkillAssessment.error.NotAcceptableException;
import com.example.GemSkillAssessment.error.NotFoundException;
import com.example.GemSkillAssessment.model.LoginRequest;
import com.example.GemSkillAssessment.model.Period;
import com.example.GemSkillAssessment.model.User;
import com.example.GemSkillAssessment.model.dto.DataDTO;
import com.example.GemSkillAssessment.model.dto.PasswordDto;
import com.example.GemSkillAssessment.model.dto.UserDto;
import com.example.GemSkillAssessment.service.period.PeriodService;
import com.example.GemSkillAssessment.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin()
@RestController
@RequestMapping(value = "/")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private PeriodService periodService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return this.createdJWT(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("/verifyAccount")
    public ResponseEntity<?> verifyAccount(@Valid @RequestBody UserDto userDto) {
        this.verifyAcount(userDto.getEmail(), userDto.getToken());
        return ResponseEntity.ok(new DataDTO(EError.SUCCESSFULLY.getLabel()));
    }

    @PostMapping("/createdPassword")
    public ResponseEntity<?> creatPassword(@Valid @RequestBody() PasswordDto passwordDto) {
        User user = userService.findByEmail(passwordDto.getEmail());
        this.verifyAcount(passwordDto.getEmail(), passwordDto.getToken());

        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmonPassword()))
            throw new NotAcceptableException(EError.CONFIRM.getLabel());
        if (user.getTokenVerify() == null) throw new NotAcceptableException(EError.ACCOUNT_NOT_INVITED.getLabel());

        userService.updatePassword(user, passwordDto.getNewPassword());
        user.setEnabled(true);
        userService.save(user);

        return this.createdJWT(user.getEmail(), passwordDto.getNewPassword());
    }

    private ResponseEntity<?> createdJWT(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse(jwt);

        User user = userService.findByEmail(username);
        Period period = periodService.findTopPeriod();
        jwtAuthenticationResponse.seteJobTitle(user.getVaEJobTitle());
        jwtAuthenticationResponse.setUserName(user.getName());
        jwtAuthenticationResponse.setIdUser(user.getId());
        if (userService.checkFillEdit(user.getPermissions(), EPermission.EDIT)) jwtAuthenticationResponse.setEdit(true);
        if (userService.checkFillEdit(user.getPermissions(), EPermission.FILL)) jwtAuthenticationResponse.setFill(true);
        if (period != null) jwtAuthenticationResponse.setIdPeriod(period.getId());

        return ResponseEntity.ok(jwtAuthenticationResponse);
    }

    private void verifyAcount(String email, String token) {
        User user = userService.findByEmail(email);
        if (user == null) throw new NotFoundException(EError.EMAIL_NOT_EXIST.getLabel());
        else if (user.getTokenVerify() == null) throw new NotAcceptableException(EError.ACCOUNT_NOT_INVITED.getLabel());
        else if (!user.getTokenVerify().equals(token))
            throw new NotFoundException(EError.TOKEN_IS_FOUNF_VERIFY.getLabel());
    }
}

