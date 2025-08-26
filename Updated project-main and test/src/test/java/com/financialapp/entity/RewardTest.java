package com.financialapp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RewardTest {

    private Reward reward;
    private User user;
    private FinancialGoal goal;
    private FinancialActivity activity;
    private RewardCatalog catalog;

    @BeforeEach
    void setUp() {
        user = new User(1, "test@example.com", false, 100);

        goal = new FinancialGoal();
        goal.setGoalId(10);
        goal.setCustomAttrs("{\"target\":1000}");
        goal.setAchieved(true);
        goal.setUser(user);

        activity = new FinancialActivity();
        activity.setActivityId(20);
        activity.setUser(user);
        activity.setActivityType(ActivityType.REFERRAL);
        activity.setActivityDate(LocalDateTime.of(2025, 8, 20, 12, 0));

        catalog = new RewardCatalog();
        catalog.setCatalogItemId(30);
        catalog.setName("Gift Voucher");
        catalog.setRewardType("VOUCHER");
        catalog.setPointsRequired(200);
        catalog.setConfiguration("{\"code\":\"ABC123\"}");
        catalog.setActive(true);
        catalog.setValidityDuration(30);

        reward = new Reward(
                100L,
                user,
                goal,
                activity,
                500,
                LocalDateTime.of(2025, 8, 21, 15, 30),
                catalog
        );
    }

    @Test
    @DisplayName("All-args constructor sets all fields")
    void testAllArgsConstructor() {
        assertEquals(100L, reward.getRewardId());
        assertEquals(user, reward.getUser());
        assertEquals(goal, reward.getGoal());
        assertEquals(activity, reward.getActivity());
        assertEquals(500, reward.getPoints());
        assertEquals(LocalDateTime.of(2025, 8, 21, 15, 30), reward.getEarnedAt());
        assertEquals(catalog, reward.getCatalogItem());
    }

    @Test
    @DisplayName("No-args constructor initializes nulls")
    void testNoArgsConstructor() {
        Reward empty = new Reward();
        assertNull(empty.getRewardId());
        assertNull(empty.getUser());
        assertNull(empty.getGoal());
        assertNull(empty.getActivity());
        assertNull(empty.getPoints());
        assertNull(empty.getEarnedAt());
        assertNull(empty.getCatalogItem());
    }

    @Test
    @DisplayName("Getters and Setters work correctly")
    void testGettersAndSetters() {
        Reward r = new Reward();
        r.setRewardId(200L);
        r.setUser(user);
        r.setGoal(goal);
        r.setActivity(activity);
        r.setPoints(250);
        r.setEarnedAt(LocalDateTime.of(2025, 1, 1, 10, 0));
        r.setCatalogItem(catalog);

        assertEquals(200L, r.getRewardId());
        assertEquals(user, r.getUser());
        assertEquals(goal, r.getGoal());
        assertEquals(activity, r.getActivity());
        assertEquals(250, r.getPoints());
        assertEquals(LocalDateTime.of(2025, 1, 1, 10, 0), r.getEarnedAt());
        assertEquals(catalog, r.getCatalogItem());
    }

    @Test
    @DisplayName("equals and hashCode should match for same values")
    void testEqualsAndHashCode() {
        Reward r1 = new Reward(300L, user, goal, activity, 100, LocalDateTime.now(), catalog);
        Reward r2 = new Reward(300L, user, goal, activity, 100, LocalDateTime.now(), catalog);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    @DisplayName("toString contains key fields")
    void testToString() {
        String str = reward.toString();
        assertTrue(str.contains("rewardId=100"));
        assertTrue(str.contains("points=500"));
        assertTrue(str.contains("earnedAt=2025-08-21T15:30"));
    }
}
