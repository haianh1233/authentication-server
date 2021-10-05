package com.haianh.authenticationserver.repository;

import com.haianh.authenticationserver.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
    Optional<Lecturer> findLecturerByName(String name);
    Optional<Lecturer> findLecturerByEmail(String email);
}
