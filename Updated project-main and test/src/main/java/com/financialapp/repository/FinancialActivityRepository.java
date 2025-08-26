package com.financialapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financialapp.entity.FinancialActivity;

public interface FinancialActivityRepository extends JpaRepository<FinancialActivity, Integer> {
}
