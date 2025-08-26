package com.financialapp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FinancialActivityTest {

    private User user;
    private FinancialActivity activity;

    @BeforeEach
    void setUp() {
        user = new User(1, "user@example.com", false, 100);
        activity = new FinancialActivity();
        activity.setActivityId(10);
        activity.setUser(user);
        activity.setActivityType(ActivityType.REFERRAL);
        activity.setActivityDate(LocalDateTime.of(2025, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("Default constructor initializes null fields")
    void testDefaultConstructor() {
        FinancialActivity fa = new FinancialActivity();
        assertNull(fa.getActivityId());
        assertNull(fa.getUser());
        assertNull(fa.getActivityType());
        assertNull(fa.getActivityDate());
    }

    @Test
    @DisplayName("All-args constructor sets fields")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        FinancialActivity fa = new FinancialActivity(20, user, ActivityType.SALARY_CREDIT, now);

        assertEquals(20, fa.getActivityId());
        assertEquals(user, fa.getUser());
        assertEquals(ActivityType.SALARY_CREDIT, fa.getActivityType());
        assertEquals(now, fa.getActivityDate());
    }

    @Test
    @DisplayName("Getters and Setters work correctly")
    void testGettersSetters() {
        assertEquals(10, activity.getActivityId());
        assertEquals(user, activity.getUser());
        assertEquals(ActivityType.REFERRAL, activity.getActivityType());
        assertEquals(LocalDateTime.of(2025, 1, 1, 12, 0), activity.getActivityDate());

        activity.setActivityType(ActivityType.HOLIDAY_BONUS);
        assertEquals(ActivityType.HOLIDAY_BONUS, activity.getActivityType());
    }

    @Test
    @DisplayName("prePersist sets date if null")
    void testPrePersistSetsDate() {
        FinancialActivity fa = new FinancialActivity();
        assertNull(fa.getActivityDate());

        fa.prePersist();

        assertNotNull(fa.getActivityDate());
    }

    @Test
    @DisplayName("prePersist does not overwrite existing date")
    void testPrePersistKeepsExistingDate() {
        LocalDateTime before = activity.getActivityDate();
        activity.prePersist();
        assertEquals(before, activity.getActivityDate());
    }

    @Test
    @DisplayName("equals, hashCode, toString from Lombok")
    void testEqualsHashCodeToString() {
        LocalDateTime now = LocalDateTime.now();
        FinancialActivity a1 = new FinancialActivity(1, user, ActivityType.PROMOTIONAL_EVENT, now);
        FinancialActivity a2 = new FinancialActivity(1, user, ActivityType.PROMOTIONAL_EVENT, now);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());

        String str = a1.toString();
        assertTrue(str.contains("activityId=1"));
        assertTrue(str.contains("activityType=PROMOTIONAL_EVENT"));
    }
}
