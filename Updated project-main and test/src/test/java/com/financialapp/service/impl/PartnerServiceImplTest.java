package com.financialapp.service.impl;

import com.financialapp.dto.PartnerDTO;
import com.financialapp.entity.Partner;
import com.financialapp.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartnerServiceImplTest {

    private PartnerRepository partnerRepository;
    private ModelMapper modelMapper;
    private PartnerServiceImpl partnerService;

    @BeforeEach
    void setUp() {
        partnerRepository = mock(PartnerRepository.class);
        modelMapper = new ModelMapper(); // real mapper (fine for DTO <-> Entity)
        partnerService = new PartnerServiceImpl();
        partnerService.partnerRepository = partnerRepository;
        partnerService.modelMapper = modelMapper;
    }

    @Test
    void testCreatePartner() {
        PartnerDTO dto = new PartnerDTO();
        dto.setId(1);
        dto.setName("Test Partner");
        dto.setApiKey("key123");

        Partner partner = modelMapper.map(dto, Partner.class);

        when(partnerRepository.save(any(Partner.class))).thenReturn(partner);

        PartnerDTO result = partnerService.createPartner(dto);

        assertNotNull(result);
        assertEquals("Test Partner", result.getName());
        assertEquals("key123", result.getApiKey());
        verify(partnerRepository, times(1)).save(any(Partner.class));
    }

    @Test
    void testGetPartnerById() {
        Partner partner = new Partner();
        partner.setId(1);
        partner.setName("Partner One");
        partner.setApiKey("api-123");

        when(partnerRepository.findById(1)).thenReturn(Optional.of(partner));

        PartnerDTO result = partnerService.getPartnerById(1);

        assertNotNull(result);
        assertEquals("Partner One", result.getName());
        assertEquals("api-123", result.getApiKey());
    }

    @Test
    void testGetAllPartners() {
        Partner p1 = new Partner();
        p1.setId(1);
        p1.setName("P1");
        p1.setApiKey("k1");

        Partner p2 = new Partner();
        p2.setId(2);
        p2.setName("P2");
        p2.setApiKey("k2");

        when(partnerRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<PartnerDTO> result = partnerService.getAllPartners();

        assertEquals(2, result.size());
        assertEquals("P1", result.get(0).getName());
        assertEquals("P2", result.get(1).getName());
    }

    @Test
    void testUpdatePartner() {
        Partner existing = new Partner();
        existing.setId(1);
        existing.setName("Old Name");
        existing.setApiKey("oldKey");

        PartnerDTO dto = new PartnerDTO();
        dto.setName("New Name");
        dto.setApiKey("newKey");

        when(partnerRepository.findById(1)).thenReturn(Optional.of(existing));
        when(partnerRepository.save(existing)).thenReturn(existing);

        PartnerDTO result = partnerService.updatePartner(1, dto);

        assertEquals("New Name", result.getName());
        assertEquals("newKey", result.getApiKey());
        verify(partnerRepository, times(1)).save(existing);
    }

    @Test
    void testDeletePartner() {
        doNothing().when(partnerRepository).deleteById(1);

        partnerService.deletePartner(1);

        verify(partnerRepository, times(1)).deleteById(1);
    }
}
