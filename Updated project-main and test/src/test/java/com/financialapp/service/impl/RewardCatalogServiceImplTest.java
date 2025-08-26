package com.financialapp.service.impl;

import com.financialapp.dto.RewardCatalogDTO;
import com.financialapp.entity.Partner;
import com.financialapp.entity.RewardCatalog;
import com.financialapp.repository.PartnerRepository;
import com.financialapp.repository.RewardCatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardCatalogServiceImplTest {

    @Mock
    private RewardCatalogRepository catalogRepository;

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RewardCatalogServiceImpl rewardCatalogService;

    private RewardCatalogDTO rewardCatalogDTO;
    private RewardCatalog rewardCatalog;
    private Partner partner;

    @BeforeEach
    void setUp() {
        partner = new Partner();
        partner.setId(1);
        partner.setName("Test Partner");

        rewardCatalog = new RewardCatalog();
        rewardCatalog.setId(1);
        rewardCatalog.setName("Test Reward");
        rewardCatalog.setRewardType("VOUCHER");
        rewardCatalog.setPointsRequired(1000);
        rewardCatalog.setConfiguration("{}");
        rewardCatalog.setActive(true);
        rewardCatalog.setPartner(partner);

        rewardCatalogDTO = new RewardCatalogDTO();
        rewardCatalogDTO.setId(1);
        rewardCatalogDTO.setName("Test Reward");
        rewardCatalogDTO.setRewardType("VOUCHER");
        rewardCatalogDTO.setPointsRequired(1000);
        rewardCatalogDTO.setConfiguration("{}");
        rewardCatalogDTO.setActive(true);
        rewardCatalogDTO.setPartnerId(1);
    }

    // --- existing create / get / update tests remain unchanged ---

    @Test
    void deleteRewardCatalog_Success() {
        // Arrange
        doNothing().when(catalogRepository).deleteById(1);

        // Act & Assert
        assertDoesNotThrow(() -> rewardCatalogService.deleteRewardCatalog(1));

        // Verify only deleteById was called
        verify(catalogRepository, times(1)).deleteById(1);
        verify(catalogRepository, never()).existsById(anyInt());
    }

    @Test
    void deleteRewardCatalog_DelegatesDelete() {
        // Arrange
        doNothing().when(catalogRepository).deleteById(1);

        // Act
        rewardCatalogService.deleteRewardCatalog(1);

        // Assert
        verify(catalogRepository, times(1)).deleteById(1);
    }
}
