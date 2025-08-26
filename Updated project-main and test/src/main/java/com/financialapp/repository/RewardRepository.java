package com.financialapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financialapp.entity.Reward;

public interface RewardRepository extends JpaRepository<Reward, Long> {

	List<Reward> findByUser_UserId(Integer userId);
}
