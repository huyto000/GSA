package com.example.GemSkillAssessment.service;

import com.example.GemSkillAssessment.error.NotAcceptableException;
import com.example.GemSkillAssessment.model.User;
import com.example.GemSkillAssessment.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class Verify {
    @Autowired
    private UserService userService;

    public void checkDataSource(List<User> userExcel, List<User> userDatabase) {
        this.checkEmail(userExcel, userDatabase);
        this.checkIdEmployee(userExcel, userDatabase);
        this.checkJobTitle(userExcel, userDatabase);
        this.checkNameSupervised(userExcel, userDatabase);
    }

    public void checkIdEmployee(List<User> userExcel, List<User> userDatabase) {
        List<String> listIdEmployeeExcel = new ArrayList<>();
        for (User user : userExcel) {
            listIdEmployeeExcel.add(user.getIdEmployee());
        }
        List<String> listIdEmployeeDatabase = new ArrayList<>();
        for (User user : userDatabase) {
            listIdEmployeeDatabase.add(user.getIdEmployee());
        }
        //Check Empty and twice apear in file
        for (String s : listIdEmployeeExcel) {
            if (Collections.frequency(listIdEmployeeExcel, s) > 1 || "".equals(s))
                throw new NotAcceptableException("IdEmployee can't be null or duplicate");
        }
    }

    public void checkJobTitle(List<User> userExcel, List<User> userDatabase) {
        for (User user : userExcel) {
            if ("".equals(user.getEJobTitle())) throw new NotAcceptableException("Job title can't be null");
        }
    }

    public void checkEmail(List<User> userExcel, List<User> userDatabase) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        List<String> listEmailExcel = new ArrayList<>();
        for (User user : userExcel) {
            listEmailExcel.add(user.getEmail());
        }
        for (String s : listEmailExcel) {
            if (Collections.frequency(listEmailExcel, s) > 1 || "".equals(s) || !s.matches(regex))
                throw new NotAcceptableException("Email not be Empty or Wrong format or dublicate");
        }
    }


    public void checkNameSupervised(List<User> userExcel, List<User> userDatabase) {
        List<String> names = new ArrayList<>();
        for (User user : userExcel) {
            names.add(user.getName());
        }
        for (User user : userExcel) {
            if (Collections.frequency(names, user.getSupervisedName()) == 0 && !"".equals(user.getSupervisedName())) {
                throw new NotAcceptableException("Supervised name can't be null or not found");
            }
        }
    }

}
