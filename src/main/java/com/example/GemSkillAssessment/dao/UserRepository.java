package com.example.GemSkillAssessment.dao;

import com.example.GemSkillAssessment.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.Date;
import java.util.List;

public interface UserRepository extends CrudRepository<User, String>, QueryByExampleExecutor<User> {
    User findById(int id);

    User findByEmail(String email);

    User findByIdEmployee(String id);

    User findByIdAndSupervisedId(Integer id, Integer id1);

    List<User> findByIsAdminFalse();

    List<User> findByIsAdminFalseAndIsLeftFalse();

    List<User> findAllByIdEmployeeIn(List<String> idEmployee);

    List<User> findAll();

    @Query(value = "SELECT id FROM user WHERE name = ?1", nativeQuery = true)
    Integer findIdByName(String name);

    @Modifying
    @Query("UPDATE User u SET u.supervisedId = :supervisedId WHERE u.id = :id")
    int updateSupervisedId(@Param("supervisedId") int supervisedId, @Param("id") int id);


    @Modifying
    @Query("UPDATE User u SET u.idEmployee = :idEmployee, u.EJobTitle = :jobTitle, u.name= :name, u.supervisedName= :supervisedName WHERE u.id= :id")
    int updateDefaultFieldUser(@Param("idEmployee") String idEmployee, @Param("jobTitle") String jobTitle, @Param("name") String name, @Param("supervisedName") String supervisedName, @Param("id") int id);

    /*@Modifying
    @Query("UPDATE User u SET u.review = true WHERE u.id = :id")
    int updateReview(@Param("id") int id);*/

}
