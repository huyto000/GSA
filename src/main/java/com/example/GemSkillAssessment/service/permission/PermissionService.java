package com.example.GemSkillAssessment.service.permission;

import com.example.GemSkillAssessment.dao.PermissionRepository;
import com.example.GemSkillAssessment.dao.UserRepository;
import com.example.GemSkillAssessment.enumerted.EPermission;
import com.example.GemSkillAssessment.model.Permission;
import com.example.GemSkillAssessment.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserRepository userRepository;

    public User lockFill(User user) {
        Permission permission = permissionRepository.findByType(EPermission.FILL);
        if (user.getPermissions().contains(permission))
            user.getPermissions().remove(permission);
        return user;
    }

    public User lockEdit(User user) {
        Permission permission = permissionRepository.findByType(EPermission.EDIT);
        if (user.getPermissions().contains(permission))
            user.getPermissions().remove(permission);
        return user;
    }


    public void openFillAll() {
        Permission permission = permissionRepository.findByType(EPermission.FILL);
        List<User> userList = userRepository.findAll();
        userList.stream().forEach(user -> {
            if (!user.getPermissions().contains(permission)) user.getPermissions().add(permission);
        });
        userRepository.saveAll(userList);
    }

}
