package com.mikko.apifintech.repository;

import com.mikko.apifintech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email")
//    boolean existsByEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
