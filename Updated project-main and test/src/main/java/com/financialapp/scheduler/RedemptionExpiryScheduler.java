package com.financialapp.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.financialapp.entity.Redemption;
import com.financialapp.entity.RedemptionStatus;
import com.financialapp.repository.RedemptionRepository;
import com.financialapp.service.RedemptionService;

import jakarta.transaction.Transactional;

@Service
public class RedemptionExpiryScheduler {

    @Autowired RedemptionRepository redemptionRepository;

    @Transactional
    @Scheduled(cron = "0 0 * * * *") // runs every hour
    public void expireRedemptions() {
        // Find all redemption that are SUCCESS and have expiryDate in the past
        List<Redemption> expiredList = redemptionRepository.findAllByStatusAndExpiryDateBefore(
                RedemptionStatus.SUCCESS, LocalDateTime.now());

        for (Redemption r : expiredList) {
            r.setStatus(RedemptionStatus.EXPIRED);
            // No refund logic here per rules
            redemptionRepository.save(r);
        }
    }
}
