package com.financialapp.service.impl;

import com.financialapp.dto.PartnerDTO;
import com.financialapp.entity.Partner;
import com.financialapp.repository.PartnerRepository;
import com.financialapp.service.PartnerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartnerServiceImpl implements PartnerService {

    @Autowired PartnerRepository partnerRepository;

    @Autowired ModelMapper modelMapper;

    @Override
    public PartnerDTO createPartner(PartnerDTO dto) {
        Partner partner = modelMapper.map(dto, Partner.class);
        return modelMapper.map(partnerRepository.save(partner), PartnerDTO.class);
    }

    @Override
    public PartnerDTO getPartnerById(Integer id) {
        Partner partner = partnerRepository.findById(id).orElseThrow();
        return modelMapper.map(partner, PartnerDTO.class);
    }

    @Override
    public List<PartnerDTO> getAllPartners() {
        return partnerRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, PartnerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PartnerDTO updatePartner(Integer id, PartnerDTO dto) {
        Partner existing = partnerRepository.findById(id).orElseThrow();
        existing.setName(dto.getName());
        existing.setApiKey(dto.getApiKey());
        return modelMapper.map(partnerRepository.save(existing), PartnerDTO.class);
    }

    @Override
    public void deletePartner(Integer id) {
        partnerRepository.deleteById(id);
    }
}
