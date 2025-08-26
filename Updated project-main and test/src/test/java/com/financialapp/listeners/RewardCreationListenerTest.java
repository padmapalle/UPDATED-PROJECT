package com.financialapp.listeners;

import com.financialapp.entity.ActivityType;
import com.financialapp.entity.FinancialActivity;
import com.financialapp.entity.FinancialGoal;
import com.financialapp.entity.GoalType;
import com.financialapp.events.ActivityCreatedEvent;
import com.financialapp.events.GoalAchievedEvent;
import com.financialapp.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RewardCreationListenerTest {

    private RewardService rewardService;
    private RewardCreationListener listener;

    @BeforeEach
    void setUp() {
        rewardService = mock(RewardService.class);
        listener = new RewardCreationListener(rewardService);
    }

    @Test
    @DisplayName("Should create reward with 200 points for INSURANCE goal")
    void testHandleGoalAchieved() {
        // given
        FinancialGoal goal = mock(FinancialGoal.class);
        when(goal.getGoalType()).thenReturn(GoalType.INSURANCE);

        GoalAchievedEvent event = new GoalAchievedEvent(this, goal);

        // when
        listener.handleGoalAchieved(event);

        // then
        verify(rewardService, times(1)).createRewardForGoal(goal, 200);
    }

    @Test
    @DisplayName("Should create reward with 200 points for REFERRAL activity")
    void testHandleActivityCreated() {
        // given
        FinancialActivity activity = mock(FinancialActivity.class);
        when(activity.getActivityType()).thenReturn(ActivityType.REFERRAL);

        ActivityCreatedEvent event = new ActivityCreatedEvent(this, activity);

        // when
        listener.handleActivityCreated(event);

        // then
        verify(rewardService, times(1)).createRewardForActivity(activity, 200);
    }

    @Test
    @DisplayName("Should assign 0 points for unknown activity type")
    void testHandleUnknownActivity() {
        // given
        FinancialActivity activity = mock(FinancialActivity.class);
        when(activity.getActivityType()).thenReturn(null);

        ActivityCreatedEvent event = new ActivityCreatedEvent(this, activity);

        // when
        listener.handleActivityCreated(event);

        // then
        verify(rewardService, times(1)).createRewardForActivity(activity, 0);
    }
}
