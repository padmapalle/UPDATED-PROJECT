package com.financialapp.service;

import com.financialapp.dto.RewardCatalogDTO;
import java.util.List;

public interface RewardCatalogService {
    RewardCatalogDTO createRewardCatalog(RewardCatalogDTO dto);
    RewardCatalogDTO getRewardCatalogById(Integer id);
    List<RewardCatalogDTO> getAllRewardCatalogs();
    RewardCatalogDTO updateRewardCatalog(Integer id, RewardCatalogDTO dto);
    void deleteRewardCatalog(Integer id);
}
