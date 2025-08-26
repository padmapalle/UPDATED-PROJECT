package com.financialapp.service.impl;

import com.financialapp.dto.RewardDTO;
import com.financialapp.entity.*;
import com.financialapp.repository.*;
import com.financialapp.service.RewardService;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FinancialGoalRepository goalRepository;

    @Autowired
    private FinancialActivityRepository activityRepository;

    @Autowired
    private RewardCatalogRepository catalogRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Reward createRewardForActivity(FinancialActivity activity, int pointsToAdd) {
        // 1. Get current points (handles NULL as 0 automatically)
        int userId = activity.getUser().getUserId();
        int currentPoints = userRepository.getCurrentPoints(userId);
        
        // 2. Create and save reward
        Reward reward = new Reward();
        reward.setUser(activity.getUser());
        reward.setActivity(activity);
        reward.setPoints(pointsToAdd);
        reward.setEarnedAt(LocalDateTime.now());
        rewardRepository.save(reward);
        
        // 3. Update user points
        int newPoints = currentPoints + pointsToAdd;
        userRepository.updatePoints(userId, newPoints);
        
        // 4. Keep in-memory user object consistent
        activity.getUser().setPoints(newPoints);
        
        return reward;
    }

    @Override
    @Transactional
    public Reward createRewardForGoal(FinancialGoal goal, int pointsToAdd) {
        // Same pattern as above
        int userId = goal.getUser().getUserId();
        int currentPoints = userRepository.getCurrentPoints(userId);
        
        Reward reward = new Reward();
        reward.setUser(goal.getUser());
        reward.setGoal(goal);
        reward.setPoints(pointsToAdd);
        reward.setEarnedAt(LocalDateTime.now());
        rewardRepository.save(reward);
        
        int newPoints = currentPoints + pointsToAdd;
        userRepository.updatePoints(userId, newPoints);
        goal.getUser().setPoints(newPoints);
        
        return reward;
    }


    
    @Override
    @Transactional
    public RewardDTO createReward(RewardDTO dto) {
        boolean hasGoalId = dto.getGoalId() != null;
        boolean hasActivityId = dto.getActivityId() != null;

        if (!(hasGoalId ^ hasActivityId)) {
            throw new IllegalArgumentException("Exactly one of goalId or activityId must be provided.");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Reward reward = new Reward();
        reward.setUser(user);

        if (hasGoalId) {
            FinancialGoal goal = goalRepository.findById(dto.getGoalId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid goalId: " + dto.getGoalId()));

            // Business rule: reward only if goal is achieved
            if (!goal.isAchieved()) {
                throw new IllegalStateException("Cannot issue reward: goal has not been achieved.");
            }

            reward.setGoal(goal);
            reward.setActivity(null);
        }

        if (hasActivityId) {
            FinancialActivity activity = activityRepository.findById(dto.getActivityId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid activityId: " + dto.getActivityId()));

            // Future: add activity-specific eligibility rules here if needed
            reward.setActivity(activity);
            reward.setGoal(null);
        }

        // ✅ Only use active catalog items
        RewardCatalog catalogItem = catalogRepository.findByCatalogItemIdAndActiveTrue(dto.getCatalogItemId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Catalog item is inactive or does not exist: " + dto.getCatalogItemId()));
        reward.setCatalogItem(catalogItem);

        reward.setPoints(dto.getPoints());
        reward.setEarnedAt(dto.getEarnedAt());

        // ✅ Auto-credit points to the user
        user.setPoints(user.getPoints() + dto.getPoints());
        userRepository.save(user);

        return modelMapper.map(rewardRepository.save(reward), RewardDTO.class);
    }

    @Override
    public RewardDTO getRewardById(Long id) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reward not found"));
        return modelMapper.map(reward, RewardDTO.class);
    }

    @Override
    public List<RewardDTO> getAllRewards() {
        return rewardRepository.findAll()
                .stream()
                .map(reward -> modelMapper.map(reward, RewardDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RewardDTO updateReward(Long id, RewardDTO dto) {
        boolean hasGoalId = dto.getGoalId() != null;
        boolean hasActivityId = dto.getActivityId() != null;

        if (!(hasGoalId ^ hasActivityId)) {
            throw new IllegalArgumentException("Exactly one of goalId or activityId must be provided.");
        }

        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reward not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        reward.setPoints(dto.getPoints());
        reward.setEarnedAt(dto.getEarnedAt());
        reward.setUser(user);

        if (hasGoalId) {
            reward.setGoal(goalRepository.findById(dto.getGoalId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid goalId: " + dto.getGoalId())));
            reward.setActivity(null);
        } else {
            reward.setActivity(activityRepository.findById(dto.getActivityId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid activityId: " + dto.getActivityId())));
            reward.setGoal(null);
        }

        // ✅ Only use active catalog items
        RewardCatalog catalogItem = catalogRepository.findByCatalogItemIdAndActiveTrue(dto.getCatalogItemId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Catalog item is inactive or does not exist: " + dto.getCatalogItemId()));
        reward.setCatalogItem(catalogItem);

        // If points changed, update user points difference
        int oldPoints = reward.getPoints();
        int newPoints = catalogItem.getPointsRequired();
        int diff = newPoints - oldPoints;
        user.setPoints(user.getPoints() + diff);
        userRepository.save(user);

        return modelMapper.map(rewardRepository.save(reward), RewardDTO.class);
    }

    @Override
    public void deleteReward(Long id) {
        rewardRepository.deleteById(id);
    }

    @Override
    public List<RewardDTO> getRewardsByUserId(Integer userId) {
        
        return rewardRepository.findByUser_UserId(userId)
                .stream()
                .map(reward -> modelMapper.map(reward, RewardDTO.class))
                .collect(Collectors.toList());
    }




}
