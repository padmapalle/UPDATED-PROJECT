package com.financialapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.financialapp.entity.Redemption;
import com.financialapp.entity.RedemptionStatus;

public interface RedemptionRepository extends JpaRepository<Redemption, Integer> {
	
	List<Redemption> findAllByStatusAndExpiryDateBefore(RedemptionStatus status, LocalDateTime dateTime);

}
