package com.financialapp.dto;

import java.util.function.IntPredicate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO {
    private Integer partnerId;
    private String name;
    private String apiKey;
    // Remove email, status, and id fields since they weren't in your original
    // These methods suggest they might be handled differently
    public IntPredicate getEmail() {
        return null;
    }
    
    public IntPredicate getStatus() {
        return null;
    }
    
    public IntPredicate getId() {
        return null;
    }
    
    public void setId(Integer id) {
    }
    
    public void setEmail(String email) {
    }
    
    public void setStatus(String status) {
    }
}