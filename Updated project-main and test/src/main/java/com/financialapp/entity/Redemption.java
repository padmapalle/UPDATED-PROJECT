package com.financialapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REDEMPTION")
public class Redemption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer redemptionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "catalog_item_id")
    private RewardCatalog catalogItem;

    private LocalDateTime redeemedAt;

    @Lob
    private String fulfillmentDetails;

    @Lob
    private String failureReason;

    private LocalDateTime expiryDate;
    private String redemptionCode;
    
    @Enumerated(EnumType.STRING)
    private RedemptionStatus status;

	public void setAmount(double d) {
		// TODO Auto-generated method stub
		
	}

	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
