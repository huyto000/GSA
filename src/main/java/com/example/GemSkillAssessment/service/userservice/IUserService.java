package com.example.GemSkillAssessment.service.userservice;

import com.example.GemSkillAssessment.model.User;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface IUserService {

    void inviteListUser(List<String> ids);

    void updateDefaultFieldUser(String employeeId, String jobTitle, String name, String supervisedName, int id);

    void updatePassword(User user, String newPassword);

    void deactiveUser(String id);

    List<User> importUser(byte[] data) throws IOException, ParseException;

    Boolean checkPassword(String oldPassword, String newPassword);

    Iterable<User> saveAll(List<User> users);

    Iterable<User> findAll();

    Integer findNameById(String name);

    void checkReview();

    public List<User> getListEmployeeFromSupervisedId(Integer id, List<User> users);

    public Set<Integer> getSetSuperviserId(List<User> users);

    public List<User> getListEmployeeFromSupervisedEmail(String email);

    public void importReview();
}
