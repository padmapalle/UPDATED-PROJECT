package com.financialapp.repository;

import com.financialapp.entity.ActivityType;
import com.financialapp.entity.FinancialActivity;
import com.financialapp.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest   // boots only JPA-related components with in-memory DB
class FinancialActivityRepositoryTest {

    @Autowired
    private FinancialActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save and find FinancialActivity by id")
    void testSaveAndFindById() {
        User user = new User(null, "repo-user@example.com", false, 50);
        user = userRepository.save(user);

        FinancialActivity activity = new FinancialActivity();
        activity.setUser(user);
        activity.setActivityType(ActivityType.REFERRAL);
        activity.setActivityDate(LocalDateTime.of(2025, 8, 20, 12, 0));

        FinancialActivity saved = activityRepository.save(activity);

        Optional<FinancialActivity> found = activityRepository.findById(saved.getActivityId());

        assertThat(found).isPresent();
        assertThat(found.get().getActivityType()).isEqualTo(ActivityType.REFERRAL);
        assertThat(found.get().getUser().getEmail()).isEqualTo("repo-user@example.com");
    }

    @Test
    @DisplayName("findAll should return all saved activities")
    void testFindAll() {
        User user = new User(null, "multi@example.com", false, 30);
        user = userRepository.save(user);

        FinancialActivity a1 = new FinancialActivity();
        a1.setUser(user);
        a1.setActivityType(ActivityType.SALARY_CREDIT);
        a1.setActivityDate(LocalDateTime.now());

        FinancialActivity a2 = new FinancialActivity();
        a2.setUser(user);
        a2.setActivityType(ActivityType.HOLIDAY_BONUS);
        a2.setActivityDate(LocalDateTime.now());

        activityRepository.save(a1);
        activityRepository.save(a2);

        List<FinancialActivity> all = activityRepository.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("deleteById removes entity")
    void testDeleteById() {
        User user = new User(null, "delete@example.com", false, 10);
        user = userRepository.save(user);

        FinancialActivity activity = new FinancialActivity();
        activity.setUser(user);
        activity.setActivityType(ActivityType.PROFILE_COMPLETION);
        activity.setActivityDate(LocalDateTime.now());

        FinancialActivity saved = activityRepository.save(activity);

        activityRepository.deleteById(saved.getActivityId());

        assertThat(activityRepository.findById(saved.getActivityId())).isEmpty();
    }
}
