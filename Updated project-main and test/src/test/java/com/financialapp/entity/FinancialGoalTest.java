package com.financialapp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinancialGoalTest {

    private FinancialGoal goal;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1, "test@example.com", false, 100);
        goal = new FinancialGoal(
                1,
                user,
                "{\"target\":5000}",
                GoalType.GROCERY,
                false
        );
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        FinancialGoal defaultGoal = new FinancialGoal();

        assertNotNull(defaultGoal);
        assertNull(defaultGoal.getGoalId());
        assertNull(defaultGoal.getUser());
        assertNull(defaultGoal.getCustomAttrs());
        assertNull(defaultGoal.getGoalType());
        assertFalse(defaultGoal.isAchieved());
    }

    @Test
    @DisplayName("Test all-args constructor")
    void testAllArgsConstructor() {
        assertNotNull(goal);
        assertEquals(1, goal.getGoalId());
        assertEquals(user, goal.getUser());
        assertEquals("{\"target\":5000}", goal.getCustomAttrs());
        assertEquals(GoalType.GROCERY, goal.getGoalType());
        assertFalse(goal.isAchieved());
    }

    @Test
    @DisplayName("Test getters and setters")
    void testGettersAndSetters() {
        FinancialGoal testGoal = new FinancialGoal();

        User newUser = new User(2, "new@example.com", true, 200);

        testGoal.setGoalId(10);
        testGoal.setUser(newUser);
        testGoal.setCustomAttrs("{\"target\":10000}");
        testGoal.setGoalType(GoalType.INVESTMENT);
        testGoal.setAchieved(true);

        assertEquals(10, testGoal.getGoalId());
        assertEquals(newUser, testGoal.getUser());
        assertEquals("{\"target\":10000}", testGoal.getCustomAttrs());
        assertEquals(GoalType.INVESTMENT, testGoal.getGoalType());
        assertTrue(testGoal.isAchieved());
    }

    @Test
    @DisplayName("Test equals() with same values")
    void testEqualsWithSameValues() {
        FinancialGoal sameGoal = new FinancialGoal(
                1,
                user,
                "{\"target\":5000}",
                GoalType.GROCERY,
                false
        );

        assertEquals(goal, sameGoal);
        assertEquals(goal.hashCode(), sameGoal.hashCode());
    }

    @Test
    @DisplayName("Test equals() with different values")
    void testEqualsWithDifferentValues() {
        FinancialGoal differentGoal = new FinancialGoal(
                2,
                user,
                "{\"target\":2000}",
                GoalType.INVESTMENT,
                true
        );

        assertNotEquals(goal, differentGoal);
    }

    @Test
    @DisplayName("Test toString() method")
    void testToString() {
        String toStringResult = goal.toString();

        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("goalId=1"));
        assertTrue(toStringResult.contains("GROCERY"));
        assertTrue(toStringResult.contains("isAchieved=false"));
    }

    @Nested
    @DisplayName("Field-specific tests")
    class FieldSpecificTests {

        @Test
        @DisplayName("Test goalId field")
        void testGoalId() {
            goal.setGoalId(999);
            assertEquals(999, goal.getGoalId());

            goal.setGoalId(null);
            assertNull(goal.getGoalId());
        }

        @Test
        @DisplayName("Test customAttrs field")
        void testCustomAttrs() {
            goal.setCustomAttrs("{\"budget\":3000}");
            assertEquals("{\"budget\":3000}", goal.getCustomAttrs());

            goal.setCustomAttrs(null);
            assertNull(goal.getCustomAttrs());
        }

        @Test
        @DisplayName("Test goalType field")
        void testGoalType() {
            goal.setGoalType(GoalType.INVESTMENT);
            assertEquals(GoalType.INVESTMENT, goal.getGoalType());

            goal.setGoalType(GoalType.INSURANCE);
            assertEquals(GoalType.INSURANCE, goal.getGoalType());

            goal.setGoalType(null);
            assertNull(goal.getGoalType());
        }

        @Test
        @DisplayName("Test isAchieved field")
        void testIsAchieved() {
            goal.setAchieved(true);
            assertTrue(goal.isAchieved());

            goal.setAchieved(false);
            assertFalse(goal.isAchieved());
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Test null user")
        void testNullUser() {
            goal.setUser(null);
            assertNull(goal.getUser());
        }

        @Test
        @DisplayName("Test empty customAttrs")
        void testEmptyCustomAttrs() {
            goal.setCustomAttrs("");
            assertEquals("", goal.getCustomAttrs());
        }

        @Test
        @DisplayName("Test goal with all null values")
        void testAllNullValues() {
            FinancialGoal nullGoal = new FinancialGoal(null, null, null, null, false);

            assertNull(nullGoal.getGoalId());
            assertNull(nullGoal.getUser());
            assertNull(nullGoal.getCustomAttrs());
            assertNull(nullGoal.getGoalType());
            assertFalse(nullGoal.isAchieved());
        }
    }
}
