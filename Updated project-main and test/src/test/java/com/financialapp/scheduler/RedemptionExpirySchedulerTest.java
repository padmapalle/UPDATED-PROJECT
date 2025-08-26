package com.financialapp.scheduler;

import com.financialapp.entity.Redemption;
import com.financialapp.entity.RedemptionStatus;
import com.financialapp.repository.RedemptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class RedemptionExpirySchedulerTest {

    private RedemptionRepository redemptionRepository;
    private RedemptionExpiryScheduler scheduler;

    @BeforeEach
    void setUp() {
        redemptionRepository = mock(RedemptionRepository.class);
        scheduler = new RedemptionExpiryScheduler();
        // Inject mock manually since field is @Autowired in prod
        scheduler.redemptionRepository = redemptionRepository;
    }

    @Test
    void testExpireRedemptions_FoundExpired() {
        // given: one redemption already expired
        Redemption redemption = new Redemption();
        redemption.setStatus(RedemptionStatus.SUCCESS);
        redemption.setExpiryDate(LocalDateTime.now().minusDays(1));

        when(redemptionRepository.findAllByStatusAndExpiryDateBefore(
                eq(RedemptionStatus.SUCCESS), any(LocalDateTime.class)))
                .thenReturn(List.of(redemption));

        // when
        scheduler.expireRedemptions();

        // then: status updated and saved
        verify(redemptionRepository, times(1)).save(redemption);
        assert redemption.getStatus() == RedemptionStatus.EXPIRED;
    }

    @Test
    void testExpireRedemptions_NoExpiredFound() {
        when(redemptionRepository.findAllByStatusAndExpiryDateBefore(
                eq(RedemptionStatus.SUCCESS), any(LocalDateTime.class)))
                .thenReturn(List.of());

        scheduler.expireRedemptions();

        // save should never be called
        verify(redemptionRepository, never()).save(any());
    }
}
