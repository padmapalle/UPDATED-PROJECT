package com.financialapp.controller;

import com.financialapp.dto.RewardCatalogDTO;
import com.financialapp.service.RewardCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reward-catalog")
public class RewardCatalogController {

    @Autowired
    private RewardCatalogService catalogService;

    @PostMapping
    public RewardCatalogDTO create(@RequestBody RewardCatalogDTO dto) {
        return catalogService.createRewardCatalog(dto);
    }

    @GetMapping("/{id}")
    public RewardCatalogDTO getById(@PathVariable Integer id) {
        return catalogService.getRewardCatalogById(id);
    }

    @GetMapping
    public List<RewardCatalogDTO> getAll() {
        return catalogService.getAllRewardCatalogs();
    }

    @PutMapping("/{id}")
    public RewardCatalogDTO update(@PathVariable Integer id, @RequestBody RewardCatalogDTO dto) {
        return catalogService.updateRewardCatalog(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        catalogService.deleteRewardCatalog(id);
    }
}
