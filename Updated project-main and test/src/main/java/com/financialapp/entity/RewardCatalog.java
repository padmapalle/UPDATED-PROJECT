package com.financialapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REWARD_CATALOG")
public class RewardCatalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer catalogItemId;

    private String name;
    private String rewardType;
    private Integer pointsRequired;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @Lob
    private String configuration;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active;
    
    @Column(name = "validity_duration")
    private Integer validityDuration;

	public void setId(int i) {
		// TODO Auto-generated method stub
		
	} 


}
