package com.example.GemSkillAssessment.dao;

import com.example.GemSkillAssessment.enumerted.EPermission;
import com.example.GemSkillAssessment.model.Permission;
import org.springframework.data.repository.CrudRepository;

public interface PermissionRepository extends CrudRepository<Permission, Integer> {
    Permission findByType(EPermission type);
}
