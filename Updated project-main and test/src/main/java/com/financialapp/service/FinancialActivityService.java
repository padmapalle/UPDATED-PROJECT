package com.financialapp.service;

import com.financialapp.dto.FinancialActivityDTO;
import java.util.List;

public interface FinancialActivityService {
    FinancialActivityDTO create(FinancialActivityDTO dto);
    FinancialActivityDTO getById(Integer id);
    List<FinancialActivityDTO> getAll();
    FinancialActivityDTO update(Integer id, FinancialActivityDTO dto);
    void delete(Integer id);
}
