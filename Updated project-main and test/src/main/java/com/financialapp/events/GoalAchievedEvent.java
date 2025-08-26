package com.financialapp.events;

import org.springframework.context.ApplicationEvent;

import com.financialapp.entity.FinancialGoal;

public class GoalAchievedEvent extends ApplicationEvent {

    private final FinancialGoal goal;

    public GoalAchievedEvent(Object source, FinancialGoal goal) {
        super(source);
        this.goal = goal;
    }

    public FinancialGoal getGoal() {
        return goal;
    }
}
