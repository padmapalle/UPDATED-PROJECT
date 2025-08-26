package com.financialapp.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardDTO {
    private Long rewardId;
    private Integer userId;
    private Integer goalId;
    private Integer activityId;
    private Integer catalogItemId;
    private Integer points;
    private LocalDateTime earnedAt;
}

