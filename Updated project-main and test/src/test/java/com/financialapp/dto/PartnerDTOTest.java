package com.financialapp.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.function.IntPredicate;

class PartnerDTOTest {

    private PartnerDTO partnerDTO;
    private PartnerDTO anotherPartnerDTO;

    @BeforeEach
    void setUp() {
        partnerDTO = new PartnerDTO(1, "Test Partner", "api-key-12345");
        anotherPartnerDTO = new PartnerDTO(2, "Another Partner", "api-key-67890");
    }

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        PartnerDTO defaultPartner = new PartnerDTO();
        
        assertNotNull(defaultPartner);
        assertNull(defaultPartner.getPartnerId());
        assertNull(defaultPartner.getName());
        assertNull(defaultPartner.getApiKey());
        
        // Test the custom methods that return IntPredicate
        assertNull(defaultPartner.getEmail());
        assertNull(defaultPartner.getStatus());
        assertNull(defaultPartner.getId());
    }

    @Test
    @DisplayName("Test all-args constructor")
    void testAllArgsConstructor() {
        assertNotNull(partnerDTO);
        assertEquals(1, partnerDTO.getPartnerId());
        assertEquals("Test Partner", partnerDTO.getName());
        assertEquals("api-key-12345", partnerDTO.getApiKey());
    }

    @Test
    @DisplayName("Test Lombok getter and setter methods")
    void testLombokGettersAndSetters() {
        PartnerDTO partner = new PartnerDTO();
        
        // Test Lombok-generated setters
        partner.setPartnerId(10);
        partner.setName("New Partner");
        partner.setApiKey("new-api-key");

        // Test Lombok-generated getters
        assertEquals(10, partner.getPartnerId());
        assertEquals("New Partner", partner.getName());
        assertEquals("new-api-key", partner.getApiKey());
    }

    @Test
    @DisplayName("Test custom email methods")
    void testCustomEmailMethods() {
        // The custom setEmail method takes String but returns IntPredicate
        partnerDTO.setEmail("test@example.com");
        
        // getEmail returns IntPredicate, not String
        IntPredicate emailPredicate = partnerDTO.getEmail();
        assertNull(emailPredicate); // Based on your implementation
    }

    @Test
    @DisplayName("Test custom status methods")
    void testCustomStatusMethods() {
        partnerDTO.setStatus("ACTIVE");
        
        IntPredicate statusPredicate = partnerDTO.getStatus();
        assertNull(statusPredicate); // Based on your implementation
    }

    @Test
    @DisplayName("Test custom id methods")
    void testCustomIdMethods() {
        partnerDTO.setId(1001);
        
        IntPredicate idPredicate = partnerDTO.getId();
        assertNull(idPredicate); // Based on your implementation
    }

    @Test
    @DisplayName("Test equals() method with same values")
    void testEqualsWithSameValues() {
        PartnerDTO samePartner = new PartnerDTO(1, "Test Partner", "api-key-12345");
        assertEquals(partnerDTO, samePartner);
    }

    @Test
    @DisplayName("Test equals() method with different values")
    void testEqualsWithDifferentValues() {
        assertNotEquals(partnerDTO, anotherPartnerDTO);
    }

    @Test
    @DisplayName("Test toString() method")
    void testToString() {
        String toStringResult = partnerDTO.toString();
        
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Test Partner"));
        assertTrue(toStringResult.contains("api-key-12345"));
    }

    @Test
    @DisplayName("Test hashCode consistency")
    void testHashCodeConsistency() {
        int initialHashCode = partnerDTO.hashCode();
        assertEquals(initialHashCode, partnerDTO.hashCode());
        
        PartnerDTO samePartner = new PartnerDTO(1, "Test Partner", "api-key-12345");
        assertEquals(partnerDTO.hashCode(), samePartner.hashCode());
    }

    @Test
    @DisplayName("Test field-specific functionality")
    void testFieldSpecificFunctionality() {
        // Test partnerId
        partnerDTO.setPartnerId(999);
        assertEquals(999, partnerDTO.getPartnerId());

        // Test name
        partnerDTO.setName("Updated Name");
        assertEquals("Updated Name", partnerDTO.getName());

        // Test apiKey
        partnerDTO.setApiKey("updated-api-key");
        assertEquals("updated-api-key", partnerDTO.getApiKey());
    }

    @Test
    @DisplayName("Test null handling")
    void testNullHandling() {
        PartnerDTO nullPartner = new PartnerDTO(null, null, null);
        
        assertNull(nullPartner.getPartnerId());
        assertNull(nullPartner.getName());
        assertNull(nullPartner.getApiKey());
        assertNull(nullPartner.getEmail());
        assertNull(nullPartner.getStatus());
        assertNull(nullPartner.getId());
    }
}