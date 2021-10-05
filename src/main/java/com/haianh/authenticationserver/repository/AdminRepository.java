package com.haianh.authenticationserver.repository;

import com.haianh.authenticationserver.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{
    Optional<Admin> findAdminByName(String name);

    @Query("select a from Admin a where a.username = ?1 and a.password = ?2")
    Optional<Admin> findAdminByUsernameAndPassword(String name, String password);
}
