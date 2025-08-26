package com.financialapp.controller;

import com.financialapp.dto.RewardDTO;
import com.financialapp.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @PostMapping
    public RewardDTO createReward(@RequestBody RewardDTO dto) {
        return rewardService.createReward(dto);
    }

    @GetMapping("/{id}")
    public RewardDTO getRewardById(@PathVariable Long id) {
        return rewardService.getRewardById(id);
    }

    @GetMapping
    public List<RewardDTO> getAllRewards() {
        return rewardService.getAllRewards();
    }

    @PutMapping("/{id}")
    public RewardDTO updateReward(@PathVariable Long id, @RequestBody RewardDTO dto) {
        return rewardService.updateReward(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteReward(@PathVariable Long id) {
        rewardService.deleteReward(id);
    }
    
    // New endpoint to get rewards by user ID
    @GetMapping("/user/{userId}")
    public List<RewardDTO> getRewardsByUserId(@PathVariable Integer userId) {
        return rewardService.getRewardsByUserId(userId);
    }
}
