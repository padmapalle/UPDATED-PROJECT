package com.financialapp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RedemptionTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        User user = new User();
        user.setUserId(101);
        user.setName("John Doe");

        RewardCatalog catalog = new RewardCatalog();
        catalog.setCatalogItemId(2001);
        catalog.setName("Reward A");

        LocalDateTime redeemedAt = LocalDateTime.now();
        LocalDateTime expiryDate = redeemedAt.plusDays(7);

        Redemption redemption = new Redemption(
                1,
                user,
                catalog,
                redeemedAt,
                "Shipped",
                null,
                expiryDate,
                "CODE123",
                RedemptionStatus.PENDING
        );

        assertEquals(1, redemption.getRedemptionId());
        assertEquals(user, redemption.getUser());
        assertEquals(catalog, redemption.getCatalogItem());
        assertEquals(redeemedAt, redemption.getRedeemedAt());
        assertEquals("Shipped", redemption.getFulfillmentDetails());
        assertNull(redemption.getFailureReason());
        assertEquals(expiryDate, redemption.getExpiryDate());
        assertEquals("CODE123", redemption.getRedemptionCode());
        assertEquals(RedemptionStatus.PENDING, redemption.getStatus());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Redemption redemption = new Redemption();

        User user = new User();
        user.setUserId(102);
        RewardCatalog catalog = new RewardCatalog();
        catalog.setCatalogItemId(2002);

        LocalDateTime redeemedAt = LocalDateTime.of(2025, 1, 1, 12, 0);
        LocalDateTime expiryDate = redeemedAt.plusDays(10);

        redemption.setRedemptionId(2);
        redemption.setUser(user);
        redemption.setCatalogItem(catalog);
        redemption.setRedeemedAt(redeemedAt);
        redemption.setFulfillmentDetails("Delivered");
        redemption.setFailureReason("None");
        redemption.setExpiryDate(expiryDate);
        redemption.setRedemptionCode("CODE456");
        redemption.setStatus(RedemptionStatus.SUCCESS);

        assertEquals(2, redemption.getRedemptionId());
        assertEquals(user, redemption.getUser());
        assertEquals(catalog, redemption.getCatalogItem());
        assertEquals(redeemedAt, redemption.getRedeemedAt());
        assertEquals("Delivered", redemption.getFulfillmentDetails());
        assertEquals("None", redemption.getFailureReason());
        assertEquals(expiryDate, redemption.getExpiryDate());
        assertEquals("CODE456", redemption.getRedemptionCode());
        assertEquals(RedemptionStatus.SUCCESS, redemption.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        Redemption r1 = new Redemption(1, new User(), new RewardCatalog(),
                now, "Details", null, now.plusDays(7), "CODE123", RedemptionStatus.PENDING);

        Redemption r2 = new Redemption(1, new User(), new RewardCatalog(),
                now, "Details", null, now.plusDays(7), "CODE123", RedemptionStatus.PENDING);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testToString() {
        Redemption redemption = new Redemption();
        redemption.setRedemptionId(5);
        redemption.setRedemptionCode("CODE999");

        String str = redemption.toString();
        assertTrue(str.contains("redemptionId=5"));
        assertTrue(str.contains("redemptionCode=CODE999"));
    }
}
