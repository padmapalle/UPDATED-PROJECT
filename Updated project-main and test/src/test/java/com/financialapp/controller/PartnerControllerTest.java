package com.financialapp.controller;

import com.financialapp.dto.PartnerDTO;
import com.financialapp.service.PartnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartnerControllerTest {

    @Mock
    private PartnerService partnerService;

    @InjectMocks
    private PartnerController partnerController;

    private PartnerDTO testPartnerDTO;

    @BeforeEach
    void setUp() {
        // Create a basic test DTO
        testPartnerDTO = new PartnerDTO();
        testPartnerDTO.setName("Test Partner");
    }

    @Test
    void testCreatePartner() {
        // Given - Mock the service to return a specific response
        PartnerDTO serviceResponse = new PartnerDTO();
        serviceResponse.setName("Test Partner");
        when(partnerService.createPartner(any(PartnerDTO.class))).thenReturn(serviceResponse);

        // When
        PartnerDTO result = partnerController.createPartner(testPartnerDTO);

        // Then - Only test what we know exists
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Partner");
        verify(partnerService, times(1)).createPartner(testPartnerDTO);
    }

    @Test
    void testGetPartnerById() {
        // Given
        PartnerDTO serviceResponse = new PartnerDTO();
        serviceResponse.setName("Test Partner");
        when(partnerService.getPartnerById(1)).thenReturn(serviceResponse);

        // When
        PartnerDTO result = partnerController.getPartnerById(1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Partner");
        verify(partnerService, times(1)).getPartnerById(1);
    }

    @Test
    void testGetAllPartners() {
        // Given
        PartnerDTO partner1 = new PartnerDTO();
        partner1.setName("Partner 1");
        
        PartnerDTO partner2 = new PartnerDTO();
        partner2.setName("Partner 2");
        
        List<PartnerDTO> partners = Arrays.asList(partner1, partner2);
        when(partnerService.getAllPartners()).thenReturn(partners);

        // When
        List<PartnerDTO> result = partnerController.getAllPartners();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Partner 1");
        assertThat(result.get(1).getName()).isEqualTo("Partner 2");
        verify(partnerService, times(1)).getAllPartners();
    }

    @Test
    void testUpdatePartner() {
        // Given
        PartnerDTO serviceResponse = new PartnerDTO();
        serviceResponse.setName("Updated Partner");
        when(partnerService.updatePartner(eq(1), any(PartnerDTO.class))).thenReturn(serviceResponse);

        // When
        PartnerDTO result = partnerController.updatePartner(1, testPartnerDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Updated Partner");
        verify(partnerService, times(1)).updatePartner(1, testPartnerDTO);
    }

    @Test
    void testDeletePartner() {
        // Given
        doNothing().when(partnerService).deletePartner(1);

        // When
        partnerController.deletePartner(1);

        // Then
        verify(partnerService, times(1)).deletePartner(1);
    }

    @Test
    void testCreatePartnerWithNullInput() {
        // Given
        when(partnerService.createPartner(null)).thenReturn(null);

        // When
        PartnerDTO result = partnerController.createPartner(null);

        // Then
        assertThat(result).isNull();
        verify(partnerService, times(1)).createPartner(null);
    }

    @Test
    void testGetPartnerByIdNotFound() {
        // Given
        when(partnerService.getPartnerById(999)).thenReturn(null);

        // When
        PartnerDTO result = partnerController.getPartnerById(999);

        // Then
        assertThat(result).isNull();
        verify(partnerService, times(1)).getPartnerById(999);
    }

    @Test
    void testGetAllPartnersEmptyList() {
        // Given
        when(partnerService.getAllPartners()).thenReturn(List.of());

        // When
        List<PartnerDTO> result = partnerController.getAllPartners();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(partnerService, times(1)).getAllPartners();
    }

    @Test
    void testPartnerWithOnlyNameField() {
        // Given - Service returns DTO with only name field populated
        PartnerDTO minimalResponse = new PartnerDTO();
        minimalResponse.setName("Minimal Partner");
        // All other fields are null
        
        when(partnerService.createPartner(any(PartnerDTO.class))).thenReturn(minimalResponse);

        // When
        PartnerDTO result = partnerController.createPartner(testPartnerDTO);

        // Then - Only test the name field, accept that others are null
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Minimal Partner");
        assertThat(result.getEmail()).isNull();
        assertThat(result.getStatus()).isNull();
        verify(partnerService, times(1)).createPartner(testPartnerDTO);
    }

    @Test
    void testPartnerWithCompletelyEmptyResponse() {
        // Given - Service returns an empty DTO (all fields null)
        PartnerDTO emptyResponse = new PartnerDTO();
        when(partnerService.createPartner(any(PartnerDTO.class))).thenReturn(emptyResponse);

        // When
        PartnerDTO result = partnerController.createPartner(testPartnerDTO);

        // Then - The DTO exists but all fields are null
        assertThat(result).isNotNull();
        assertThat(result.getName()).isNull();
        assertThat(result.getEmail()).isNull();
        assertThat(result.getStatus()).isNull();
        verify(partnerService, times(1)).createPartner(testPartnerDTO);
    }

    @Test
    void testControllerSimplyPassesThroughData() {
        // Given - The controller's job is just to pass data to service
        PartnerDTO anyResponse = new PartnerDTO();
        anyResponse.setName("Any Response");
        when(partnerService.createPartner(any(PartnerDTO.class))).thenReturn(anyResponse);

        // When
        PartnerDTO result = partnerController.createPartner(testPartnerDTO);

        // Then - Don't test specific field values, just that it passes through
        assertThat(result).isNotNull();
        verify(partnerService, times(1)).createPartner(testPartnerDTO);
    }
}