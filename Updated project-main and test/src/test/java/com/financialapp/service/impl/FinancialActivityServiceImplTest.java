package com.financialapp.service.impl;

import com.financialapp.dto.FinancialActivityDTO;
import com.financialapp.entity.ActivityType;
import com.financialapp.entity.FinancialActivity;
import com.financialapp.entity.User;
import com.financialapp.events.ActivityCreatedEvent;
import com.financialapp.repository.FinancialActivityRepository;
import com.financialapp.repository.UserRepository;
import com.financialapp.service.FinancialActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialActivityServiceImplTest {

    @Mock private FinancialActivityRepository activityRepository;
    @Mock private UserRepository userRepository;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private FinancialActivityServiceImpl service;

    private User user;
    private FinancialActivity entityNew;     // before save
    private FinancialActivity entitySaved;   // after save
    private FinancialActivityDTO dtoIn;
    private FinancialActivityDTO dtoOut;

    @BeforeEach
    void setup() {
        user = new User(1, "user@example.com", false, 100);

        entityNew = new FinancialActivity();
        entityNew.setActivityId(null);
        entityNew.setUser(null); // service sets it from userRepository
        entityNew.setActivityType(ActivityType.REFERRAL);
        entityNew.setActivityDate(LocalDateTime.of(2025, 1, 1, 10, 0));

        entitySaved = new FinancialActivity();
        entitySaved.setActivityId(10);
        entitySaved.setUser(user);
        entitySaved.setActivityType(ActivityType.REFERRAL);
        entitySaved.setActivityDate(entityNew.getActivityDate());

        dtoIn = new FinancialActivityDTO(
                null, 1, ActivityType.REFERRAL, entityNew.getActivityDate()
        );
        dtoOut = new FinancialActivityDTO(
                10, 1, ActivityType.REFERRAL, entitySaved.getActivityDate()
        );
    }

    @Test
    @DisplayName("create: maps, loads user, saves, publishes event, returns DTO")
    void create_savesAndPublishesEvent() {
        // map DTO -> Entity (without user)
        when(modelMapper.map(dtoIn, FinancialActivity.class)).thenReturn(entityNew);
        // load user
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        // save (note: service saves twice)
        when(activityRepository.save(any(FinancialActivity.class))).thenReturn(entitySaved);
        // map Entity -> DTO
        when(modelMapper.map(entitySaved, FinancialActivityDTO.class)).thenReturn(dtoOut);

        FinancialActivityDTO result = service.create(dtoIn);

        assertThat(result).isEqualTo(dtoOut);

        // verify user was set & repos called
        verify(userRepository).findById(1);
        verify(activityRepository, times(2)).save(any(FinancialActivity.class)); // because method saves twice
        // verify event
        ArgumentCaptor<ActivityCreatedEvent> eventCaptor = ArgumentCaptor.forClass(ActivityCreatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getActivity()).isEqualTo(entitySaved);
    }

    @Test
    @DisplayName("getById: finds entity, maps to DTO, sets userId")
    void getById_mapsAndSetsUserId() {
        when(activityRepository.findById(10)).thenReturn(Optional.of(entitySaved));

        // mapper returns DTO without userId; service fills it from entity.user.userId
        FinancialActivityDTO mapped = new FinancialActivityDTO(10, null, ActivityType.REFERRAL, entitySaved.getActivityDate());
        when(modelMapper.map(entitySaved, FinancialActivityDTO.class)).thenReturn(mapped);

        FinancialActivityDTO result = service.getById(10);

        assertThat(result.getActivityId()).isEqualTo(10);
        assertThat(result.getUserId()).isEqualTo(1);
        assertThat(result.getActivityType()).isEqualTo(ActivityType.REFERRAL);
        verify(activityRepository).findById(10);
    }

    @Test
    @DisplayName("getAll: maps list and sets userId on each DTO")
    void getAll_mapsList() {
        FinancialActivity a2 = new FinancialActivity();
        a2.setActivityId(11);
        a2.setUser(user);
        a2.setActivityType(ActivityType.HOLIDAY_BONUS);
        a2.setActivityDate(LocalDateTime.of(2025, 2, 2, 12, 0));

        when(activityRepository.findAll()).thenReturn(List.of(entitySaved, a2));

        FinancialActivityDTO mapped1 = new FinancialActivityDTO(10, null, ActivityType.REFERRAL, entitySaved.getActivityDate());
        FinancialActivityDTO mapped2 = new FinancialActivityDTO(11, null, ActivityType.HOLIDAY_BONUS, a2.getActivityDate());
        when(modelMapper.map(entitySaved, FinancialActivityDTO.class)).thenReturn(mapped1);
        when(modelMapper.map(a2, FinancialActivityDTO.class)).thenReturn(mapped2);

        List<FinancialActivityDTO> result = service.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserId()).isEqualTo(1);
        assertThat(result.get(1).getUserId()).isEqualTo(1);
        verify(activityRepository).findAll();
    }

    @Test
    @DisplayName("update: maps DTO onto existing, saves, returns mapped DTO with userId")
    void update_mapsAndSaves() {
        FinancialActivity existing = new FinancialActivity();
        existing.setActivityId(10);
        existing.setUser(user);
        existing.setActivityType(ActivityType.REFERRAL);
        existing.setActivityDate(LocalDateTime.of(2025, 1, 1, 10, 0));

        FinancialActivityDTO patch = new FinancialActivityDTO(null, 1, ActivityType.PROMOTIONAL_EVENT,
                LocalDateTime.of(2025, 3, 3, 14, 0));

        FinancialActivity saved = new FinancialActivity();
        saved.setActivityId(10);
        saved.setUser(user);
        saved.setActivityType(ActivityType.PROMOTIONAL_EVENT);
        saved.setActivityDate(patch.getActivityDate());

        FinancialActivityDTO mappedBack = new FinancialActivityDTO(10, null, ActivityType.PROMOTIONAL_EVENT, patch.getActivityDate());

        when(activityRepository.findById(10)).thenReturn(Optional.of(existing));

        // modelMapper.map(patch, existing) -> just simulate it by adjusting existing in an Answer
        doAnswer(inv -> {
            FinancialActivityDTO src = inv.getArgument(0);
            FinancialActivity dest = inv.getArgument(1);
            dest.setActivityType(src.getActivityType());
            dest.setActivityDate(src.getActivityDate());
            return null;
        }).when(modelMapper).map(eq(patch), eq(existing));

        when(activityRepository.save(existing)).thenReturn(saved);
        when(modelMapper.map(saved, FinancialActivityDTO.class)).thenReturn(mappedBack);

        FinancialActivityDTO result = service.update(10, patch);

        assertThat(result.getActivityId()).isEqualTo(10);
        assertThat(result.getUserId()).isEqualTo(1); // set by service after mapping back
        assertThat(result.getActivityType()).isEqualTo(ActivityType.PROMOTIONAL_EVENT);
        assertThat(result.getActivityDate()).isEqualTo(patch.getActivityDate());

        verify(activityRepository).findById(10);
        verify(activityRepository).save(existing);
    }

    @Test
    @DisplayName("delete: delegates to repository")
    void delete_delegates() {
        service.delete(77);
        verify(activityRepository).deleteById(77);
    }
}
