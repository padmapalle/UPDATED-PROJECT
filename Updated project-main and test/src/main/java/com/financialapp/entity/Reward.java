package com.financialapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REWARD")
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private FinancialGoal goal;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private FinancialActivity activity;

    private Integer points;
    private LocalDateTime earnedAt;

    @ManyToOne
    @JoinColumn(name = "catalog_item_id")
    private RewardCatalog catalogItem;
}
