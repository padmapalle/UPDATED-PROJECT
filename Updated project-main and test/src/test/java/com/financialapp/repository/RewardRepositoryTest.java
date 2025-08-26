package com.financialapp.repository;

import com.financialapp.entity.Reward;
import com.financialapp.entity.RewardCatalog;
import com.financialapp.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RewardRepositoryTest {

    @Autowired private RewardRepository rewardRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RewardCatalogRepository rewardCatalogRepository;

    @Test
    @DisplayName("findByUser_UserId returns only the rewards for the specified user")
    void findByUser_UserId_returnsOnlyForThatUser() {
        // Users
        User u1 = userRepository.save(new User(null, "u1@example.com", false, 0));
        User u2 = userRepository.save(new User(null, "u2@example.com", false, 0));

        // Catalog (optional on Reward, but good to include a valid ref)
        RewardCatalog cat = rewardCatalogRepository.save(
                new RewardCatalog(null, "Voucher 100", "VOUCHER", 100, null, "{}", true, 30)
        );

        // Rewards
        rewardRepository.save(new Reward(null, u1, null, null, 50, LocalDateTime.now(), cat));
        rewardRepository.save(new Reward(null, u1, null, null, 75, LocalDateTime.now(), cat));
        rewardRepository.save(new Reward(null, u2, null, null, 25, LocalDateTime.now(), cat));

        // Query for u1
        List<Reward> u1Rewards = rewardRepository.findByUser_UserId(u1.getUserId());
        assertThat(u1Rewards).hasSize(2);
        assertThat(u1Rewards).allMatch(r -> r.getUser().getUserId().equals(u1.getUserId()));
        assertThat(u1Rewards).extracting(Reward::getPoints).containsExactlyInAnyOrder(50, 75);

        // Query for u2
        List<Reward> u2Rewards = rewardRepository.findByUser_UserId(u2.getUserId());
        assertThat(u2Rewards).hasSize(1);
        assertThat(u2Rewards.get(0).getPoints()).isEqualTo(25);
    }

    @Test
    @DisplayName("Basic CRUD: save, findById, deleteById")
    void basicCrud() {
        User user = userRepository.save(new User(null, "crud@example.com", false, 0));
        RewardCatalog cat = rewardCatalogRepository.save(
                new RewardCatalog(null, "Gift 200", "BONUS", 200, null, "{}", true, 15)
        );

        Reward saved = rewardRepository.save(
                new Reward(null, user, null, null, 200, LocalDateTime.now(), cat)
        );

        assertThat(saved.getRewardId()).isNotNull();
        assertThat(rewardRepository.findById(saved.getRewardId())).isPresent();

        rewardRepository.deleteById(saved.getRewardId());
        assertThat(rewardRepository.findById(saved.getRewardId())).isEmpty();
    }

    @Test
    @DisplayName("findByUser_UserId returns empty list when user has no rewards")
    void findByUser_UserId_emptyWhenNoRewards() {
        User user = userRepository.save(new User(null, "norewards@example.com", false, 0));
        List<Reward> results = rewardRepository.findByUser_UserId(user.getUserId());
        assertThat(results).isEmpty();
    }
}
