package com.financialapp.controller;

import com.financialapp.dto.RedemptionDTO;
import com.financialapp.service.RedemptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/redemptions")
public class RedemptionController {

    @Autowired
    private RedemptionService redemptionService;

    @PostMapping
    public RedemptionDTO createRedemption(@RequestBody RedemptionDTO dto) {
        return redemptionService.createRedemption(dto);
    }

    @GetMapping("/{id}")
    public RedemptionDTO getRedemptionById(@PathVariable Integer id) {
        return redemptionService.getRedemptionById(id);
    }

    @GetMapping
    public List<RedemptionDTO> getAllRedemptions() {
        return redemptionService.getAllRedemptions();
    }

    @PutMapping("/{id}")
    public RedemptionDTO updateRedemption(@PathVariable Integer id, @RequestBody RedemptionDTO dto) {
        return redemptionService.updateRedemption(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteRedemption(@PathVariable Integer id) {
        redemptionService.deleteRedemption(id);
    }
}
