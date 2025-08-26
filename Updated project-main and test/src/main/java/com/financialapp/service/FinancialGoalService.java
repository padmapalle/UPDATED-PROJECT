package com.financialapp.service;

import com.financialapp.dto.FinancialGoalDTO;
import java.util.List;

public interface FinancialGoalService {
    FinancialGoalDTO createGoal(FinancialGoalDTO dto);
    FinancialGoalDTO getGoalById(Integer id);
    List<FinancialGoalDTO> getAllGoals();
    FinancialGoalDTO updateGoal(Integer id, FinancialGoalDTO dto);
    void deleteGoal(Integer id);

    FinancialGoalDTO markGoalAsAchieved(Integer id);
}
