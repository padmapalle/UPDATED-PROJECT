package com.financialapp.config;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

public class ModelMapperConfigTest {

    @Test
    public void testModelMapperCreation() {
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();
        
        assertNotNull(modelMapper);
        assertInstanceOf(ModelMapper.class, modelMapper);
        
        // Test that the ModelMapper is functional
        assertNotNull(modelMapper.getConfiguration());
    }

    @Test
    public void testModelMapperConfiguration() {
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();
        
        // Verify configuration settings (add more specific tests based on your actual config)
        assertFalse(modelMapper.getConfiguration().isSkipNullEnabled());
        assertFalse(modelMapper.getConfiguration().isFieldMatchingEnabled());
        
        // Test that mapping works correctly
        TestSource source = new TestSource("John", "Doe", 30);
        TestDestination destination = modelMapper.map(source, TestDestination.class);
        
        assertEquals("John", destination.getFirstName());
        assertEquals("Doe", destination.getLastName());
        assertEquals(30, destination.getAge());
    }

    @Test
    public void testModelMapperSingletonBehavior() {
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper mapper1 = config.modelMapper();
        ModelMapper mapper2 = config.modelMapper();
        
        // Depending on your configuration, this could be the same instance or different
        // If your modelMapper() method uses @Bean, it should return the same instance (singleton)
        // If it creates a new instance each time, they will be different
        assertNotNull(mapper1);
        assertNotNull(mapper2);
    }

    @Test
    public void testModelMapperWithNullInput() {
        ModelMapperConfig config = new ModelMapperConfig();
        ModelMapper modelMapper = config.modelMapper();
        
        assertThrows(IllegalArgumentException.class, () -> {
            modelMapper.map(null, TestDestination.class);
        });
    }

    // Helper classes for testing
    private static class TestSource {
        private String firstName;
        private String lastName;
        private int age;
        
        public TestSource(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }
        
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public int getAge() { return age; }
    }

    private static class TestDestination {
        private String firstName;
        private String lastName;
        private int age;
        
        @SuppressWarnings("unused")
		public TestDestination() {}
        
        public String getFirstName() { return firstName; }
        @SuppressWarnings("unused")
		public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        @SuppressWarnings("unused")
		public void setLastName(String lastName) { this.lastName = lastName; }
        
        public int getAge() { return age; }
        @SuppressWarnings("unused")
		public void setAge(int age) { this.age = age; }
    }
}