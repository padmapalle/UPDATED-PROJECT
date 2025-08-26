package com.financialapp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

class PartnerTest {

    private Partner partner;
    private Partner anotherPartner;

    @BeforeEach
    void setUp() {
        partner = new Partner(1, "Test Partner", "api-key-12345");
        anotherPartner = new Partner(2, "Another Partner", "api-key-67890");
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        Partner defaultPartner = new Partner();
        
        assertNotNull(defaultPartner);
        assertNull(defaultPartner.getPartnerId());
        assertNull(defaultPartner.getName());
        assertNull(defaultPartner.getApiKey());
    }

    @Test
    @DisplayName("Test all-args constructor")
    void testAllArgsConstructor() {
        assertNotNull(partner);
        assertEquals(1, partner.getPartnerId());
        assertEquals("Test Partner", partner.getName());
        assertEquals("api-key-12345", partner.getApiKey());
    }

    @Test
    @DisplayName("Test getter and setter methods")
    void testGettersAndSetters() {
        Partner testPartner = new Partner();
        
        // Test setters
        testPartner.setPartnerId(10);
        testPartner.setName("New Partner");
        testPartner.setApiKey("new-api-key");

        // Test getters
        assertEquals(10, testPartner.getPartnerId());
        assertEquals("New Partner", testPartner.getName());
        assertEquals("new-api-key", testPartner.getApiKey());
    }

    @Test
    @DisplayName("Test equals() method with same values")
    void testEqualsWithSameValues() {
        Partner samePartner = new Partner(1, "Test Partner", "api-key-12345");

        assertEquals(partner, samePartner);
        assertEquals(partner.hashCode(), samePartner.hashCode());
    }

    @Test
    @DisplayName("Test equals() method with different values")
    void testEqualsWithDifferentValues() {
        assertNotEquals(partner, anotherPartner);
        assertNotEquals(partner.hashCode(), anotherPartner.hashCode());
    }

    @Test
    @DisplayName("Test equals() with null object")
    void testEqualsWithNull() {
        assertNotEquals(partner, null);
    }

    @Test
    @DisplayName("Test equals() with different class")
    void testEqualsWithDifferentClass() {
        assertNotEquals(partner, "Not a Partner");
    }

    @Test
    @DisplayName("Test toString() method")
    void testToString() {
        String toStringResult = partner.toString();
        
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Test Partner"));
        assertTrue(toStringResult.contains("api-key-12345"));
        assertTrue(toStringResult.contains("partnerId=1"));
    }

    @Test
    @DisplayName("Test hashCode consistency")
    void testHashCodeConsistency() {
        int initialHashCode = partner.hashCode();
        assertEquals(initialHashCode, partner.hashCode());
    }

    @Test
    @DisplayName("Test hashCode with same values")
    void testHashCodeWithSameValues() {
        Partner copy = new Partner(
            partner.getPartnerId(),
            partner.getName(),
            partner.getApiKey()
        );

        assertEquals(partner.hashCode(), copy.hashCode());
    }

    @Nested
    @DisplayName("Field-specific tests")
    class FieldSpecificTests {
        
        @Test
        @DisplayName("Test partnerId field")
        void testPartnerId() {
            partner.setPartnerId(999);
            assertEquals(999, partner.getPartnerId());
            
            // Test null value
            partner.setPartnerId(null);
            assertNull(partner.getPartnerId());
        }

        @Test
        @DisplayName("Test name field")
        void testName() {
            partner.setName("Updated Name");
            assertEquals("Updated Name", partner.getName());
            
            // Test empty string
            partner.setName("");
            assertEquals("", partner.getName());
            
            // Test null value
            partner.setName(null);
            assertNull(partner.getName());
        }

        @Test
        @DisplayName("Test apiKey field")
        void testApiKey() {
            partner.setApiKey("updated-api-key");
            assertEquals("updated-api-key", partner.getApiKey());
            
            // Test long API key
            String longApiKey = "key-" + "a".repeat(100);
            partner.setApiKey(longApiKey);
            assertEquals(longApiKey, partner.getApiKey());
            
            // Test null value
            partner.setApiKey(null);
            assertNull(partner.getApiKey());
        }
    }

    @Nested
    @DisplayName("Edge case tests")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Test all null values")
        void testAllNullValues() {
            Partner nullPartner = new Partner(null, null, null);
            
            assertNull(nullPartner.getPartnerId());
            assertNull(nullPartner.getName());
            assertNull(nullPartner.getApiKey());
        }

        @Test
        @DisplayName("Test empty string values")
        void testEmptyStringValues() {
            Partner emptyPartner = new Partner(0, "", "");
            
            assertEquals(0, emptyPartner.getPartnerId());
            assertEquals("", emptyPartner.getName());
            assertEquals("", emptyPartner.getApiKey());
        }

        @Test
        @DisplayName("Test equals with partially null values")
        void testEqualsWithPartialNulls() {
            Partner partner1 = new Partner(null, "Same Name", "Same Key");
            Partner partner2 = new Partner(null, "Same Name", "Same Key");
            
            assertEquals(partner1, partner2);
        }
    }

    @Test
    @DisplayName("Test entity creation with different scenarios")
    void testEntityCreationScenarios() {
        // Test with zero ID
        Partner zeroIdPartner = new Partner(0, "Zero ID", "zero-key");
        assertEquals(0, zeroIdPartner.getPartnerId());
        
        // Test with special characters
        Partner specialCharPartner = new Partner(100, "Test@Partner#123", "key!@#$%");
        assertEquals("Test@Partner#123", specialCharPartner.getName());
        assertEquals("key!@#$%", specialCharPartner.getApiKey());
        
        // Test with very long values
        String longName = "A".repeat(255);
        String longApiKey = "B".repeat(500);
        Partner longValuesPartner = new Partner(999, longName, longApiKey);
        assertEquals(longName, longValuesPartner.getName());
        assertEquals(longApiKey, longValuesPartner.getApiKey());
    }
}