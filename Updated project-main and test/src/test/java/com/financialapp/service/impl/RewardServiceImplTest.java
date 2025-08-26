package com.financialapp.service.impl;

import com.financialapp.dto.RewardDTO;
import com.financialapp.entity.*;
import com.financialapp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

    @Mock private RewardRepository rewardRepository;
    @Mock private UserRepository userRepository;
    @Mock private FinancialGoalRepository goalRepository;
    @Mock private FinancialActivityRepository activityRepository;
    @Mock private RewardCatalogRepository catalogRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private RewardServiceImpl rewardService;

    private User user;
    private FinancialGoal goal;
    private FinancialActivity activity;
    private RewardCatalog catalog;
    private Reward reward;

    @BeforeEach
    void setUp() {
        user = new User(1, "test@example.com", false, 100);
        goal = new FinancialGoal(10, user, "{}", GoalType.INVESTMENT, true);
        activity = new FinancialActivity(20, user, ActivityType.REFERRAL, LocalDateTime.now());
        catalog = new RewardCatalog(30, "Gift", "VOUCHER", 200, null, "{}", true, 30);

        reward = new Reward(100L, user, goal, activity, 500, LocalDateTime.now(), catalog);
    }

    @Test
    @DisplayName("createRewardForActivity adds points and returns Reward")
    void createRewardForActivity() {
        when(userRepository.getCurrentPoints(1)).thenReturn(100);
        when(rewardRepository.save(any(Reward.class))).thenAnswer(inv -> {
            Reward r = inv.getArgument(0);
            r.setRewardId(999L);
            return r;
        });

        Reward created = rewardService.createRewardForActivity(activity, 50);

        assertThat(created.getPoints()).isEqualTo(50);
        assertThat(created.getActivity()).isEqualTo(activity);
        verify(userRepository).updatePoints(1, 150);
        verify(rewardRepository).save(any(Reward.class));
    }

    @Test
    @DisplayName("createRewardForGoal adds points and returns Reward")
    void createRewardForGoal() {
        when(userRepository.getCurrentPoints(1)).thenReturn(100);
        when(rewardRepository.save(any(Reward.class))).thenAnswer(inv -> inv.getArgument(0));

        Reward created = rewardService.createRewardForGoal(goal, 75);

        assertThat(created.getPoints()).isEqualTo(75);
        assertThat(created.getGoal()).isEqualTo(goal);
        verify(userRepository).updatePoints(1, 175);
        verify(rewardRepository).save(any(Reward.class));
    }

    @Test
    @DisplayName("createReward throws when both goalId and activityId provided (XOR rule)")
    void createReward_invalid_bothProvided() {
        RewardDTO dto = new RewardDTO(null, 1, 10, 20, 30, 200, LocalDateTime.now());
        assertThatThrownBy(() -> rewardService.createReward(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Exactly one of goalId or activityId must be provided");
        verifyNoInteractions(userRepository, goalRepository, activityRepository, catalogRepository, rewardRepository);
    }

    @Test
    @DisplayName("createReward with activity: saves reward, returns mapped DTO, auto-credits user points")
    void createReward_withActivity() {
        LocalDateTime ts = LocalDateTime.now();
        RewardDTO dtoIn = new RewardDTO(null, 1, null, 20, 30, 200, ts);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(activityRepository.findById(20)).thenReturn(Optional.of(activity));
        when(catalogRepository.findByCatalogItemIdAndActiveTrue(30)).thenReturn(Optional.of(catalog));
        when(rewardRepository.save(any(Reward.class))).thenReturn(reward);

        // stub mapping Reward -> RewardDTO
        RewardDTO dtoOut = new RewardDTO(100L, 1, null, 20, 30, 200, ts);
        when(modelMapper.map(any(Reward.class), eq(RewardDTO.class))).thenReturn(dtoOut);

        RewardDTO result = rewardService.createReward(dtoIn);

        assertThat(result.getRewardId()).isEqualTo(100L);
        assertThat(result.getUserId()).isEqualTo(1);
        assertThat(result.getActivityId()).isEqualTo(20);
        assertThat(result.getCatalogItemId()).isEqualTo(30);
        assertThat(result.getPoints()).isEqualTo(200);

        // user points auto-increase by dto points
        assertThat(user.getPoints()).isEqualTo(300);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("getRewardById returns mapped DTO")
    void getRewardById_ok() {
        when(rewardRepository.findById(100L)).thenReturn(Optional.of(reward));
        RewardDTO mapped = new RewardDTO(100L, 1, 10, 20, 30, 500, reward.getEarnedAt());
        when(modelMapper.map(reward, RewardDTO.class)).thenReturn(mapped);

        RewardDTO dto = rewardService.getRewardById(100L);

        assertThat(dto.getRewardId()).isEqualTo(100L);
        assertThat(dto.getPoints()).isEqualTo(500);
        verify(rewardRepository).findById(100L);
    }

    @Test
    @DisplayName("getAllRewards returns mapped list")
    void getAllRewards_ok() {
        when(rewardRepository.findAll()).thenReturn(List.of(reward));
        RewardDTO mapped = new RewardDTO(100L, 1, 10, 20, 30, 500, reward.getEarnedAt());
        when(modelMapper.map(reward, RewardDTO.class)).thenReturn(mapped);

        List<RewardDTO> list = rewardService.getAllRewards();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getRewardId()).isEqualTo(100L);
        verify(rewardRepository).findAll();
    }

    @Test
    @DisplayName("updateReward updates entity and returns mapped DTO")
    void updateReward_ok() {
        LocalDateTime ts = LocalDateTime.now();
        RewardDTO patch = new RewardDTO(100L, 1, null, 20, 30, 600, ts);

        when(rewardRepository.findById(100L)).thenReturn(Optional.of(reward));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(activityRepository.findById(20)).thenReturn(Optional.of(activity));
        when(catalogRepository.findByCatalogItemIdAndActiveTrue(30)).thenReturn(Optional.of(catalog));
        when(rewardRepository.save(any(Reward.class))).thenReturn(reward);

        RewardDTO mapped = new RewardDTO(100L, 1, null, 20, 30, 600, ts);
        when(modelMapper.map(any(Reward.class), eq(RewardDTO.class))).thenReturn(mapped);

        RewardDTO updated = rewardService.updateReward(100L, patch);

        assertThat(updated.getPoints()).isEqualTo(600);
        assertThat(updated.getActivityId()).isEqualTo(20);
        verify(rewardRepository).save(any(Reward.class));
    }

    @Test
    @DisplayName("deleteReward delegates to repository")
    void deleteReward_ok() {
        rewardService.deleteReward(77L);
        verify(rewardRepository).deleteById(77L);
    }

    @Test
    @DisplayName("getRewardsByUserId delegates to repository and maps results")
    void getRewardsByUserId_ok() {
        when(rewardRepository.findByUser_UserId(1)).thenReturn(List.of(reward));
        RewardDTO mapped = new RewardDTO(100L, 1, 10, 20, 30, 500, reward.getEarnedAt());
        when(modelMapper.map(reward, RewardDTO.class)).thenReturn(mapped);

        List<RewardDTO> list = rewardService.getRewardsByUserId(1);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getUserId()).isEqualTo(1);
        verify(rewardRepository).findByUser_UserId(1);
    }
}
