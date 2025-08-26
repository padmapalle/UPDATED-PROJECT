package com.financialapp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RewardCatalogTest {

    @Test
    void testGettersAndSetters() {
        Partner partner = new Partner();
        partner.setPartnerId(1);
        partner.setName("Partner1");

        RewardCatalog catalog = new RewardCatalog();
        catalog.setCatalogItemId(10);
        catalog.setName("Reward1");
        catalog.setRewardType("GiftCard");
        catalog.setPointsRequired(500);
        catalog.setPartner(partner);
        catalog.setConfiguration("Config data");
        catalog.setActive(true);
        catalog.setValidityDuration(30);

        assertEquals(10, catalog.getCatalogItemId());
        assertEquals("Reward1", catalog.getName());
        assertEquals("GiftCard", catalog.getRewardType());
        assertEquals(500, catalog.getPointsRequired());
        assertEquals(partner, catalog.getPartner());
        assertEquals("Config data", catalog.getConfiguration());
        assertTrue(catalog.getActive());
        assertEquals(30, catalog.getValidityDuration());
    }

    @Test
    void testAllArgsConstructor() {
        Partner partner = new Partner();
        RewardCatalog catalog = new RewardCatalog(
                20,
                "Reward2",
                "Voucher",
                1000,
                partner,
                "Config2",
                false,
                60
        );

        assertEquals(20, catalog.getCatalogItemId());
        assertEquals("Reward2", catalog.getName());
        assertEquals("Voucher", catalog.getRewardType());
        assertEquals(1000, catalog.getPointsRequired());
        assertEquals(partner, catalog.getPartner());
        assertEquals("Config2", catalog.getConfiguration());
        assertFalse(catalog.getActive());
        assertEquals(60, catalog.getValidityDuration());
    }

    @Test
    void testEqualsAndHashCode() {
        Partner partner = new Partner();

        RewardCatalog c1 = new RewardCatalog(1, "Reward", "GiftCard", 500, partner, "Config", true, 30);
        RewardCatalog c2 = new RewardCatalog(1, "Reward", "GiftCard", 500, partner, "Config", true, 30);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToStringNotNull() {
        RewardCatalog catalog = new RewardCatalog();
        assertNotNull(catalog.toString());
    }
}
