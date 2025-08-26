package com.financialapp.dto;

import lombok.*;
import java.time.LocalDateTime;

import com.financialapp.entity.ActivityType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialActivityDTO {
    private Integer activityId;
    private Integer userId;
    private ActivityType activityType;  

    private LocalDateTime activityDate;
}
