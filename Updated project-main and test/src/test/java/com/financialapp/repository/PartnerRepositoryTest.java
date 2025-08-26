package com.financialapp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.financialapp.entity.Partner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.show-sql=true"
})
@DisplayName("Partner Repository Tests")
class PartnerRepositoryTest {

    @Autowired
    private PartnerRepository partnerRepository;

    private Partner partner1;
    private Partner partner2;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        partnerRepository.deleteAll();
        
        // Create test partners
        partner1 = new Partner();
        partner1.setName("Test Partner 1");
        partner1.setApiKey("api-key-12345");
        
        partner2 = new Partner();
        partner2.setName("Test Partner 2");
        partner2.setApiKey("api-key-67890");
        
        // Save to database
        partner1 = partnerRepository.save(partner1);
        partner2 = partnerRepository.save(partner2);
    }

    @Test
    @DisplayName("Test findAll() method")
    void testFindAll() {
        List<Partner> partners = partnerRepository.findAll();
        
        assertNotNull(partners);
        assertEquals(2, partners.size());
        assertTrue(partners.stream().anyMatch(p -> p.getName().equals("Test Partner 1")));
        assertTrue(partners.stream().anyMatch(p -> p.getName().equals("Test Partner 2")));
    }

    @Test
    @DisplayName("Test findById() with existing ID")
    void testFindByIdExisting() {
        Optional<Partner> foundPartner = partnerRepository.findById(partner1.getPartnerId());
        
        assertTrue(foundPartner.isPresent());
        assertEquals(partner1.getName(), foundPartner.get().getName());
        assertEquals(partner1.getApiKey(), foundPartner.get().getApiKey());
    }

    @Test
    @DisplayName("Test findById() with non-existing ID")
    void testFindByIdNonExisting() {
        Optional<Partner> foundPartner = partnerRepository.findById(999);
        
        assertFalse(foundPartner.isPresent());
    }

    @Test
    @DisplayName("Test save() new partner")
    void testSaveNewPartner() {
        Partner newPartner = new Partner();
        newPartner.setName("New Partner");
        newPartner.setApiKey("new-api-key");
        
        Partner savedPartner = partnerRepository.save(newPartner);
        
        assertNotNull(savedPartner);
        assertNotNull(savedPartner.getPartnerId());
        assertEquals("New Partner", savedPartner.getName());
        assertEquals("new-api-key", savedPartner.getApiKey());
        
        // Verify it's actually saved
        Optional<Partner> retrievedPartner = partnerRepository.findById(savedPartner.getPartnerId());
        assertTrue(retrievedPartner.isPresent());
    }

    @Test
    @DisplayName("Test save() update existing partner")
    void testSaveUpdatePartner() {
        // Update partner1
        partner1.setName("Updated Name");
        partner1.setApiKey("updated-api-key");
        
        Partner updatedPartner = partnerRepository.save(partner1);
        
        assertEquals("Updated Name", updatedPartner.getName());
        assertEquals("updated-api-key", updatedPartner.getApiKey());
        
        // Verify update persisted
        Optional<Partner> retrievedPartner = partnerRepository.findById(partner1.getPartnerId());
        assertTrue(retrievedPartner.isPresent());
        assertEquals("Updated Name", retrievedPartner.get().getName());
    }

    @Test
    @DisplayName("Test deleteById()")
    void testDeleteById() {
        partnerRepository.deleteById(partner1.getPartnerId());
        
        Optional<Partner> deletedPartner = partnerRepository.findById(partner1.getPartnerId());
        assertFalse(deletedPartner.isPresent());
        
        // Verify other partner still exists
        Optional<Partner> remainingPartner = partnerRepository.findById(partner2.getPartnerId());
        assertTrue(remainingPartner.isPresent());
    }

    @Test
    @DisplayName("Test count()")
    void testCount() {
        long count = partnerRepository.count();
        assertEquals(2, count);
        
        partnerRepository.deleteById(partner1.getPartnerId());
        count = partnerRepository.count();
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Test existsById() with existing ID")
    void testExistsByIdExisting() {
        boolean exists = partnerRepository.existsById(partner1.getPartnerId());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Test existsById() with non-existing ID")
    void testExistsByIdNonExisting() {
        boolean exists = partnerRepository.existsById(999);
        assertFalse(exists);
    }

    @Test
    @DisplayName("Test save() with null values")
    void testSaveWithNullValues() {
        Partner partnerWithNulls = new Partner();
        partnerWithNulls.setName(null);
        partnerWithNulls.setApiKey(null);
        
        Partner savedPartner = partnerRepository.save(partnerWithNulls);
        
        assertNotNull(savedPartner);
        assertNotNull(savedPartner.getPartnerId());
        assertNull(savedPartner.getName());
        assertNull(savedPartner.getApiKey());
    }
}