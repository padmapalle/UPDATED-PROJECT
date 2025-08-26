package com.financialapp.service.impl;

import com.financialapp.dto.FinancialGoalDTO;
import com.financialapp.dto.RewardDTO;
import com.financialapp.entity.FinancialGoal;
import com.financialapp.entity.User;
import com.financialapp.events.GoalAchievedEvent;
import com.financialapp.repository.FinancialGoalRepository;
import com.financialapp.repository.UserRepository;
import com.financialapp.service.FinancialGoalService;
import com.financialapp.service.RewardService;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialGoalServiceImpl implements FinancialGoalService {

    @Autowired
    private FinancialGoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RewardService rewardService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public FinancialGoalDTO createGoal(FinancialGoalDTO dto) {
        FinancialGoal goal = modelMapper.map(dto, FinancialGoal.class);
        goal = goalRepository.save(goal);

        // If already achieved at creation, trigger event
        if (goal.isAchieved()) {
            eventPublisher.publishEvent(new GoalAchievedEvent(this, goal));
        }

        return modelMapper.map(goal, FinancialGoalDTO.class);
    }

    @Override
    public FinancialGoalDTO getGoalById(Integer id) {
        FinancialGoal goal = goalRepository.findById(id).orElseThrow();
        FinancialGoalDTO dto = modelMapper.map(goal, FinancialGoalDTO.class);
        dto.setUserId(goal.getUser().getUserId());
        return dto;
    }

    @Override
    public List<FinancialGoalDTO> getAllGoals() {
        return goalRepository.findAll().stream()
            .map(goal -> {
                FinancialGoalDTO dto = modelMapper.map(goal, FinancialGoalDTO.class);
                dto.setUserId(goal.getUser().getUserId());
                return dto;
            }).collect(Collectors.toList());
    }

    @Override
    public FinancialGoalDTO updateGoal(Integer id, FinancialGoalDTO dto) {
        FinancialGoal existing = goalRepository.findById(id).orElseThrow();


        existing.setCustomAttrs(dto.getCustomAttrs());

        boolean wasAchievedBefore = existing.isAchieved();
        existing.setAchieved(dto.isAchieved());

        FinancialGoal saved = goalRepository.save(existing);

        // If goal just got achieved, publish event
        if (!wasAchievedBefore && saved.isAchieved()) {
            eventPublisher.publishEvent(new GoalAchievedEvent(this, saved));
        }

        return modelMapper.map(saved, FinancialGoalDTO.class);
    }


    @Override
    public void deleteGoal(Integer id) {
        goalRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public FinancialGoalDTO markGoalAsAchieved(Integer id) {
        FinancialGoal goal = goalRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

        if (!goal.isAchieved()) {
            goal.setAchieved(true);
            goalRepository.save(goal);

            // Trigger event instead of creating reward here
            eventPublisher.publishEvent(new GoalAchievedEvent(this, goal));
        }

        return modelMapper.map(goal, FinancialGoalDTO.class);
    }

}
