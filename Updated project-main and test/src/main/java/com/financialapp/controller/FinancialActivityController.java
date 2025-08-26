package com.financialapp.controller;

import com.financialapp.dto.FinancialActivityDTO;
import com.financialapp.service.FinancialActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class FinancialActivityController {

    @Autowired
    private FinancialActivityService activityService;

    @PostMapping
    public FinancialActivityDTO create(@RequestBody FinancialActivityDTO dto) {
        return activityService.create(dto);
    }

    @GetMapping("/{id}")
    public FinancialActivityDTO getById(@PathVariable Integer id) {
        return activityService.getById(id);
    }

    @GetMapping
    public List<FinancialActivityDTO> getAll() {
        return activityService.getAll();
    }

    @PutMapping("/{id}")
    public FinancialActivityDTO update(@PathVariable Integer id, @RequestBody FinancialActivityDTO dto) {
        return activityService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        activityService.delete(id);
    }
}
