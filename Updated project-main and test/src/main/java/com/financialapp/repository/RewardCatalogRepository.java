package com.financialapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.financialapp.entity.RewardCatalog;

import java.util.Optional;

public interface RewardCatalogRepository extends JpaRepository<RewardCatalog, Integer> {

	Optional<RewardCatalog> findByCatalogItemIdAndActiveTrue(Integer catalogItemId);

}
