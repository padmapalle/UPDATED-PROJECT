package com.financialapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financialapp.entity.FinancialGoal;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Integer> {
}
