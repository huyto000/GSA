package com.example.GemSkillAssessment.dao;

import com.example.GemSkillAssessment.model.JWT;
import org.springframework.data.repository.CrudRepository;

public interface JWTRepository extends CrudRepository<JWT, String> {
    JWT findByCode(String codeJWT);
}
