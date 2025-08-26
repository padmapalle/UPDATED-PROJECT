package com.financialapp.controller;

import com.financialapp.dto.FinancialGoalDTO;
import com.financialapp.service.FinancialGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class FinancialGoalController {

    @Autowired
    private FinancialGoalService goalService;

    @PostMapping
    public FinancialGoalDTO createGoal(@RequestBody FinancialGoalDTO dto) {
        return goalService.createGoal(dto);
    }

    @GetMapping("/{id}")
    public FinancialGoalDTO getGoal(@PathVariable Integer id) {
        return goalService.getGoalById(id);
    }

    @GetMapping
    public List<FinancialGoalDTO> getAllGoals() {
        return goalService.getAllGoals();
    }

    @PutMapping("/{id}")
    public FinancialGoalDTO updateGoal(@PathVariable Integer id, @RequestBody FinancialGoalDTO dto) {
        return goalService.updateGoal(id, dto);
    }
    
    @PutMapping("/{id}/achieve")
    public FinancialGoalDTO markGoalAsAchieved(@PathVariable Integer id) {
        return goalService.markGoalAsAchieved(id);
    }

    @DeleteMapping("/{id}")
    public void deleteGoal(@PathVariable Integer id) {
        goalService.deleteGoal(id);
    }
}
