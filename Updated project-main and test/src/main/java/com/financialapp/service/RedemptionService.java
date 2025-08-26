package com.financialapp.service;

import com.financialapp.dto.RedemptionDTO;
import java.util.List;

public interface RedemptionService {
    RedemptionDTO createRedemption(RedemptionDTO dto);
    RedemptionDTO getRedemptionById(Integer id);
    List<RedemptionDTO> getAllRedemptions();
    RedemptionDTO updateRedemption(Integer id, RedemptionDTO dto);
    void deleteRedemption(Integer id);
}
