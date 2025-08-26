package com.financialapp.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FINANCIAL_GOAL")
public class FinancialGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer goalId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    private String customAttrs;
    
    @Enumerated(EnumType.STRING)
    private GoalType GoalType;

    @JsonProperty("isAchieved")
    private boolean isAchieved;

}


