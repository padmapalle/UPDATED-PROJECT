package com.financialapp.service;

import com.financialapp.dto.PartnerDTO;
import java.util.List;

public interface PartnerService {
    PartnerDTO createPartner(PartnerDTO dto);
    PartnerDTO getPartnerById(Integer id);
    List<PartnerDTO> getAllPartners();
    PartnerDTO updatePartner(Integer id, PartnerDTO dto);
    void deletePartner(Integer id);
}
