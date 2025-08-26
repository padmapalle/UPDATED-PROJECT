package com.financialapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;

import com.financialapp.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    @Query("SELECT COALESCE(u.points, 0) FROM User u WHERE u.userId = :userId")
    int getCurrentPoints(@Param("userId") int userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.points = :newPoints WHERE u.userId = :userId")
    void updatePoints(@Param("userId") int userId, @Param("newPoints") int points);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE User u SET u.points = u.points + :delta WHERE u.userId = :userId")
    void addPoints(@Param("userId") int userId, @Param("delta") int pointsToAdd);

    Optional<User> findByEmail(String email);
}
