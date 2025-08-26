package com.financialapp.dto;

import lombok.*;
import java.time.LocalDateTime;

import com.financialapp.entity.RedemptionStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionDTO {
    private Integer redemptionId;
    private Integer userId;
    private Integer catalogItemId;
    private LocalDateTime redeemedAt;
    private RedemptionStatus status;
    private String fulfillmentDetails;
    private String failureReason;
    private LocalDateTime expiryDate;
    private String redemptionCode;
}
