package com.financialapp.service;

import com.financialapp.dto.RewardDTO;
import com.financialapp.entity.FinancialActivity;
import com.financialapp.entity.FinancialGoal;
import com.financialapp.entity.Reward;

import java.util.List;

public interface RewardService {
    RewardDTO createReward(RewardDTO dto);
    RewardDTO getRewardById(Long id);
    List<RewardDTO> getAllRewards();
    RewardDTO updateReward(Long id, RewardDTO dto);
    void deleteReward(Long id);
    Reward createRewardForActivity(FinancialActivity activity, int points);
    Reward createRewardForGoal(FinancialGoal goal, int points);

    List<RewardDTO> getRewardsByUserId(Integer userId);
}
