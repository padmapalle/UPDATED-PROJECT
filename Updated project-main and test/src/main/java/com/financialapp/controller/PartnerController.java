package com.financialapp.controller;

import com.financialapp.dto.PartnerDTO;
import com.financialapp.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    @Autowired
    private PartnerService partnerService;

    @PostMapping
    public PartnerDTO createPartner(@RequestBody PartnerDTO dto) {
        return partnerService.createPartner(dto);
    }

    @GetMapping("/{id}")
    public PartnerDTO getPartnerById(@PathVariable Integer id) {
        return partnerService.getPartnerById(id);
    }

    @GetMapping
    public List<PartnerDTO> getAllPartners() {
        return partnerService.getAllPartners();
    }

    @PutMapping("/{id}")
    public PartnerDTO updatePartner(@PathVariable Integer id, @RequestBody PartnerDTO dto) {
        return partnerService.updatePartner(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletePartner(@PathVariable Integer id) {
        partnerService.deletePartner(id);
    }
}
