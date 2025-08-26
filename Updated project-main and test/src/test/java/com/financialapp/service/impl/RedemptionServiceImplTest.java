package com.financialapp.service.impl;

import com.financialapp.dto.RedemptionDTO;
import com.financialapp.entity.Redemption;
import com.financialapp.entity.RedemptionStatus;
import com.financialapp.entity.RewardCatalog;
import com.financialapp.entity.User;
import com.financialapp.repository.RedemptionRepository;
import com.financialapp.repository.RewardCatalogRepository;
import com.financialapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedemptionServiceImplTest {

    private RedemptionRepository redemptionRepository;
    private UserRepository userRepository;
    private RewardCatalogRepository catalogRepository;
    private ModelMapper modelMapper;
    private RedemptionServiceImpl service;

    private User user;
    private RewardCatalog catalog;
    private Redemption redemption;

    @BeforeEach
    void setUp() {
        redemptionRepository = mock(RedemptionRepository.class);
        userRepository = mock(UserRepository.class);
        catalogRepository = mock(RewardCatalogRepository.class);
        modelMapper = new ModelMapper();

        service = new RedemptionServiceImpl();
        service.redemptionRepository = redemptionRepository;
        service.setUserRepository(userRepository);
        service.setCatalogRepository(catalogRepository);
        service.setModelMapper(modelMapper);

        // common test objects
        user = new User();
        user.setUserId(1);
        user.setPoints(200);

        catalog = new RewardCatalog();
        catalog.setCatalogItemId(10);
        catalog.setPointsRequired(100);
        catalog.setValidityDuration(30); // 30 days

        redemption = new Redemption();
        redemption.setRedemptionId(99);
        redemption.setUser(user);
        redemption.setCatalogItem(catalog);
        redemption.setStatus(RedemptionStatus.PENDING);
    }

    @Test
    void testCreateRedemption_Success() {
        RedemptionDTO dto = new RedemptionDTO();
        dto.setUserId(1);
        dto.setCatalogItemId(10);
        dto.setStatus(RedemptionStatus.SUCCESS);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(catalogRepository.findByCatalogItemIdAndActiveTrue(10)).thenReturn(Optional.of(catalog));
        when(redemptionRepository.save(any(Redemption.class))).thenAnswer(inv -> {
            Redemption saved = inv.getArgument(0);
            saved.setRedemptionId(500);
            return saved;
        });

        RedemptionDTO result = service.createRedemption(dto);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals(10, result.getCatalogItemId());
        assertEquals(RedemptionStatus.SUCCESS, result.getStatus());
        verify(userRepository).save(user);
    }

    @Test
    void testCreateRedemption_InsufficientPoints() {
        user.setPoints(50); // less than catalog cost
        RedemptionDTO dto = new RedemptionDTO();
        dto.setUserId(1);
        dto.setCatalogItemId(10);
        dto.setStatus(RedemptionStatus.SUCCESS);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(catalogRepository.findByCatalogItemIdAndActiveTrue(10)).thenReturn(Optional.of(catalog));

        assertThrows(IllegalStateException.class, () -> service.createRedemption(dto));
    }

    @Test
    void testGetRedemptionById() {
        redemption.setStatus(RedemptionStatus.SUCCESS);
        when(redemptionRepository.findById(99)).thenReturn(Optional.of(redemption));

        RedemptionDTO dto = service.getRedemptionById(99);

        assertEquals(1, dto.getUserId());
        assertEquals(10, dto.getCatalogItemId());
        assertEquals(RedemptionStatus.SUCCESS, dto.getStatus());
    }

    @Test
    void testGetAllRedemptions() {
        when(redemptionRepository.findAll()).thenReturn(List.of(redemption));

        List<RedemptionDTO> list = service.getAllRedemptions();

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getUserId());
    }

    @Test
    void testUpdateRedemption_StatusChangeToSuccess() {
        redemption.setStatus(RedemptionStatus.PENDING);

        RedemptionDTO dto = new RedemptionDTO();
        dto.setStatus(RedemptionStatus.SUCCESS);

        when(redemptionRepository.findById(99)).thenReturn(Optional.of(redemption));
        when(redemptionRepository.save(any(Redemption.class))).thenAnswer(inv -> inv.getArgument(0));

        RedemptionDTO result = service.updateRedemption(99, dto);

        assertEquals(RedemptionStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getRedeemedAt());
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateRedemption_RefundAfterSuccess() {
        redemption.setStatus(RedemptionStatus.SUCCESS);
        redemption.setRedeemedAt(LocalDateTime.now());
        redemption.setExpiryDate(LocalDateTime.now().plusDays(30));

        RedemptionDTO dto = new RedemptionDTO();
        dto.setStatus(RedemptionStatus.REFUNDED);

        when(redemptionRepository.findById(99)).thenReturn(Optional.of(redemption));
        when(redemptionRepository.save(any(Redemption.class))).thenAnswer(inv -> inv.getArgument(0));

        int beforePoints = user.getPoints();

        RedemptionDTO result = service.updateRedemption(99, dto);

        assertEquals(RedemptionStatus.REFUNDED, result.getStatus());
        assertTrue(user.getPoints() > beforePoints);
    }

    @Test
    void testDeleteRedemption() {
        service.deleteRedemption(123);
        verify(redemptionRepository).deleteById(123);
    }
}
