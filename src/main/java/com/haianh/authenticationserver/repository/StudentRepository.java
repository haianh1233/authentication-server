package com.haianh.authenticationserver.repository;

import com.haianh.authenticationserver.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findStudentByName(String name);
    Optional<Student> findStudentByEmail(String email);
}
