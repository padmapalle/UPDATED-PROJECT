package com.financialapp.listeners;

import com.financialapp.entity.ActivityType;
import com.financialapp.entity.FinancialGoal;
import com.financialapp.events.ActivityCreatedEvent;
import com.financialapp.events.GoalAchievedEvent;
import com.financialapp.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RewardCreationListener {

    private final RewardService rewardService;

    @Autowired
    public RewardCreationListener(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @EventListener
    public void handleGoalAchieved(GoalAchievedEvent event) {
        var goal = event.getGoal();
        // Set points based on goal type
        int points = calculatePointsForGoal(goal);
        rewardService.createRewardForGoal(goal, points);
    }

    @EventListener
    public void handleActivityCreated(ActivityCreatedEvent event) {
        var activity = event.getActivity();
        // Set points based on activity type (null-safe)
        int points = calculatePointsForActivity(activity.getActivityType());
        rewardService.createRewardForActivity(activity, points);
    }

    private int calculatePointsForGoal(FinancialGoal goal) {
        return switch (goal.getGoalType()) {
            case GROCERY -> 100;
            case INVESTMENT -> 150;
            case INSURANCE -> 200;
        };
    }

    int calculatePointsForActivity(ActivityType type) {
        if (type == null) {
            return 0; // safeguard against null
        }
        switch (type) {
            case REFERRAL: return 200;
            case SALARY_CREDIT: return 150;
            case ACCOUNT_ANNIVERSARY: return 100;
            case PROFILE_COMPLETION: return 50;
            case FIRST_10_UNIQUE_MERCHANTS: return 300;
            case PROMOTIONAL_EVENT: return 250;
            case HOLIDAY_BONUS: return 500;
            case MONTHLY_ACTIVE_USER: return 75;
            default: return 0;
        }
    }
}
