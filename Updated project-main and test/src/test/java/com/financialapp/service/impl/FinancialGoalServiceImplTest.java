package com.financialapp.service.impl;

import com.financialapp.dto.FinancialGoalDTO;
import com.financialapp.entity.FinancialGoal;
import com.financialapp.entity.GoalType;
import com.financialapp.entity.User;
import com.financialapp.events.GoalAchievedEvent;
import com.financialapp.repository.FinancialGoalRepository;
import com.financialapp.repository.UserRepository;
import com.financialapp.service.RewardService;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialGoalServiceImplTest {

    @Mock private FinancialGoalRepository goalRepository;
    @Mock private UserRepository userRepository;
    @Mock private RewardService rewardService;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private FinancialGoalServiceImpl service;

    private User user;
    private FinancialGoal entity;
    private FinancialGoal savedEntity;
    private FinancialGoalDTO dtoIn;
    private FinancialGoalDTO dtoOut;

    @BeforeEach
    void init() {
        user = new User(1, "test@example.com", false, 100);

        entity = new FinancialGoal();
        entity.setGoalId(null);
        entity.setUser(user);
        entity.setCustomAttrs("{\"target\":5000}");
        entity.setGoalType(GoalType.GROCERY);
        entity.setAchieved(false);

        savedEntity = new FinancialGoal();
        savedEntity.setGoalId(10);
        savedEntity.setUser(user);
        savedEntity.setCustomAttrs("{\"target\":5000}");
        savedEntity.setGoalType(GoalType.GROCERY);
        savedEntity.setAchieved(false);

        dtoIn = new FinancialGoalDTO(
                null,          // goalId
                1,             // userId
                GoalType.GROCERY, // NOTE: your DTO field name is 'GoalType' but Lombok generates getGoalType()
                "{\"target\":5000}",
                false
        );

        dtoOut = new FinancialGoalDTO(
                10,
                1,
                GoalType.GROCERY,
                "{\"target\":5000}",
                false
        );
    }

    @Test
    @DisplayName("createGoal: saves via repo and returns mapped DTO; no event if not achieved")
    void createGoal_savesAndReturnsDto_noEventWhenNotAchieved() {
        // map DTO -> Entity
        when(modelMapper.map(dtoIn, FinancialGoal.class)).thenReturn(entity);
        // repo save
        when(goalRepository.save(entity)).thenReturn(savedEntity);
        // map Entity -> DTO
        when(modelMapper.map(savedEntity, FinancialGoalDTO.class)).thenReturn(dtoOut);

        FinancialGoalDTO result = service.createGoal(dtoIn);

        assertThat(result).isEqualTo(dtoOut);
        verify(goalRepository).save(entity);
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("createGoal: publishes GoalAchievedEvent if created as achieved=true")
    void createGoal_publishesEventWhenAchieved() {
        dtoIn.setAchieved(true);
        entity.setAchieved(true);
        savedEntity.setAchieved(true);
        dtoOut.setAchieved(true);

        when(modelMapper.map(dtoIn, FinancialGoal.class)).thenReturn(entity);
        when(goalRepository.save(entity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, FinancialGoalDTO.class)).thenReturn(dtoOut);

        FinancialGoalDTO result = service.createGoal(dtoIn);

        assertThat(result.isAchieved()).isTrue();
        ArgumentCaptor<GoalAchievedEvent> eventCaptor = ArgumentCaptor.forClass(GoalAchievedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getGoal()).isEqualTo(savedEntity);
    }

    @Test
    @DisplayName("getGoalById: maps entity to DTO and sets userId from nested User")
    void getGoalById_mapsWithUserId() {
        when(goalRepository.findById(10)).thenReturn(Optional.of(savedEntity));

        // mapper returns a DTO (userId will be overridden by service from entity.getUser().getUserId())
        FinancialGoalDTO mapped = new FinancialGoalDTO(10, null, GoalType.GROCERY, "{\"target\":5000}", false);
        when(modelMapper.map(savedEntity, FinancialGoalDTO.class)).thenReturn(mapped);

        FinancialGoalDTO result = service.getGoalById(10);

        assertThat(result.getGoalId()).isEqualTo(10);
        assertThat(result.getUserId()).isEqualTo(1); // set by service
        assertThat(result.getGoalType()).isEqualTo(GoalType.GROCERY);
        verify(goalRepository).findById(10);
    }

    @Test
    @DisplayName("getAllGoals: maps a list and sets userId per item")
    void getAllGoals_returnsList() {
        FinancialGoal another = new FinancialGoal();
        another.setGoalId(11);
        another.setUser(user);
        another.setCustomAttrs("{}");
        another.setGoalType(GoalType.INSURANCE);
        another.setAchieved(true);

        when(goalRepository.findAll()).thenReturn(List.of(savedEntity, another));

        FinancialGoalDTO mapped1 = new FinancialGoalDTO(10, null, GoalType.GROCERY, "{\"target\":5000}", false);
        FinancialGoalDTO mapped2 = new FinancialGoalDTO(11, null, GoalType.INSURANCE, "{}", true);

        when(modelMapper.map(savedEntity, FinancialGoalDTO.class)).thenReturn(mapped1);
        when(modelMapper.map(another, FinancialGoalDTO.class)).thenReturn(mapped2);

        List<FinancialGoalDTO> result = service.getAllGoals();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserId()).isEqualTo(1);
        assertThat(result.get(1).getUserId()).isEqualTo(1);
        verify(goalRepository).findAll();
    }

    @Test
    @DisplayName("updateGoal: updates customAttrs & achieved; publishes event only on false->true transition")
    void updateGoal_updatesAndPublishesOnTransition() {
        FinancialGoal existing = new FinancialGoal();
        existing.setGoalId(10);
        existing.setUser(user);
        existing.setCustomAttrs("{\"target\":1000}");
        existing.setGoalType(GoalType.GROCERY);
        existing.setAchieved(false); // was false

        FinancialGoalDTO patch = new FinancialGoalDTO(
                null, 1, GoalType.GROCERY, "{\"target\":2000}", true // set achieved -> true
        );

        FinancialGoal afterSave = new FinancialGoal();
        afterSave.setGoalId(10);
        afterSave.setUser(user);
        afterSave.setCustomAttrs("{\"target\":2000}");
        afterSave.setGoalType(GoalType.GROCERY);
        afterSave.setAchieved(true);

        FinancialGoalDTO mappedResult = new FinancialGoalDTO(
                10, 1, GoalType.GROCERY, "{\"target\":2000}", true
        );

        when(goalRepository.findById(10)).thenReturn(Optional.of(existing));
        when(goalRepository.save(existing)).thenReturn(afterSave);
        when(modelMapper.map(afterSave, FinancialGoalDTO.class)).thenReturn(mappedResult);

        FinancialGoalDTO result = service.updateGoal(10, patch);

        assertThat(result.isAchieved()).isTrue();
        assertThat(result.getCustomAttrs()).isEqualTo("{\"target\":2000}");
        // event fired because false -> true
        verify(eventPublisher).publishEvent(any(GoalAchievedEvent.class));
    }

    @Test
    @DisplayName("updateGoal: no event when achieved stays false")
    void updateGoal_noEventWhenStillFalse() {
        FinancialGoal existing = new FinancialGoal();
        existing.setGoalId(10);
        existing.setUser(user);
        existing.setCustomAttrs("{}");
        existing.setGoalType(GoalType.GROCERY);
        existing.setAchieved(false);

        FinancialGoalDTO patch = new FinancialGoalDTO(null, 1, GoalType.GROCERY, "{\"x\":1}", false);

        FinancialGoal afterSave = new FinancialGoal();
        afterSave.setGoalId(10);
        afterSave.setUser(user);
        afterSave.setCustomAttrs("{\"x\":1}");
        afterSave.setGoalType(GoalType.GROCERY);
        afterSave.setAchieved(false);

        FinancialGoalDTO mappedResult = new FinancialGoalDTO(10, 1, GoalType.GROCERY, "{\"x\":1}", false);

        when(goalRepository.findById(10)).thenReturn(Optional.of(existing));
        when(goalRepository.save(existing)).thenReturn(afterSave);
        when(modelMapper.map(afterSave, FinancialGoalDTO.class)).thenReturn(mappedResult);

        FinancialGoalDTO result = service.updateGoal(10, patch);

        assertThat(result.isAchieved()).isFalse();
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("deleteGoal: delegates to repository")
    void deleteGoal_delegatesToRepo() {
        service.deleteGoal(33);
        verify(goalRepository).deleteById(33);
    }

    @Test
    @DisplayName("markGoalAsAchieved: sets achieved=true & publishes event only if previously false")
    void markGoalAsAchieved_setsAndPublishes() {
        FinancialGoal notAchieved = new FinancialGoal();
        notAchieved.setGoalId(77);
        notAchieved.setUser(user);
        notAchieved.setCustomAttrs("{}");
        notAchieved.setGoalType(GoalType.INVESTMENT);
        notAchieved.setAchieved(false);

        FinancialGoal afterSave = new FinancialGoal();
        afterSave.setGoalId(77);
        afterSave.setUser(user);
        afterSave.setCustomAttrs("{}");
        afterSave.setGoalType(GoalType.INVESTMENT);
        afterSave.setAchieved(true);

        FinancialGoalDTO mapped = new FinancialGoalDTO(77, 1, GoalType.INVESTMENT, "{}", true);

        when(goalRepository.findById(77)).thenReturn(Optional.of(notAchieved));
        when(goalRepository.save(notAchieved)).thenReturn(afterSave);
        when(modelMapper.map(afterSave, FinancialGoalDTO.class)).thenReturn(mapped);

        FinancialGoalDTO result = service.markGoalAsAchieved(77);

        assertThat(result.isAchieved()).isTrue();
        verify(eventPublisher).publishEvent(any(GoalAchievedEvent.class));
    }

    @Test
    @DisplayName("markGoalAsAchieved: no publish/save when already achieved")
    void markGoalAsAchieved_noOpIfAlreadyTrue() {
        FinancialGoal achieved = new FinancialGoal();
        achieved.setGoalId(88);
        achieved.setUser(user);
        achieved.setCustomAttrs("{}");
        achieved.setGoalType(GoalType.INSURANCE);
        achieved.setAchieved(true);

        FinancialGoalDTO mapped = new FinancialGoalDTO(88, 1, GoalType.INSURANCE, "{}", true);

        when(goalRepository.findById(88)).thenReturn(Optional.of(achieved));
        when(modelMapper.map(achieved, FinancialGoalDTO.class)).thenReturn(mapped);

        FinancialGoalDTO result = service.markGoalAsAchieved(88);

        assertThat(result.isAchieved()).isTrue();
        verify(goalRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }
}
