package com.financialapp.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardCatalogDTO {
    private Integer catalogItemId;
    private String name;
    private String rewardType;
    private Integer pointsRequired;
    private Integer partnerId; // Only the ID for simplicity

    private String configuration;
    private boolean active;
    private Integer validityDuration;
	public Short getId() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setDescription(String string) {
		// TODO Auto-generated method stub
		
	}
	public void setId(int i) {
		// TODO Auto-generated method stub
		
	} 
}
