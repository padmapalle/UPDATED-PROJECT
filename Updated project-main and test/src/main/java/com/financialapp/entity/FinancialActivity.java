package com.financialapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FINANCIAL_ACTIVITY")
public class FinancialActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer activityId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;


    private LocalDateTime activityDate;
    
    @PrePersist
    public void prePersist() {
        if (activityDate == null) {
            activityDate = LocalDateTime.now();
        }
    }
}
