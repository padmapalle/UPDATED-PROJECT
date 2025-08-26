package com.financialapp.events;

import com.financialapp.entity.FinancialGoal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalAchievedEventTest {

    @Test
    void testGoalAchievedEventStoresSourceAndGoal() {
        // Arrange
        Object source = new Object();
        FinancialGoal goal = mock(FinancialGoal.class);

        // Act
        GoalAchievedEvent event = new GoalAchievedEvent(source, goal);

        // Assert
        assertEquals(source, event.getSource(), "Source should match the one passed in");
        assertEquals(goal, event.getGoal(), "Goal should match the one passed in");
    }

    @Test
    void testGetGoal_NotNull() {
        FinancialGoal goal = mock(FinancialGoal.class);
        GoalAchievedEvent event = new GoalAchievedEvent(this, goal);

        assertNotNull(event.getGoal(), "Goal should not be null");
    }
}
