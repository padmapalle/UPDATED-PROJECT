package com.financialapp.repository;

import com.financialapp.entity.FinancialGoal;
import com.financialapp.entity.GoalType;
import com.financialapp.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // boots only JPA/Hibernate + H2
class FinancialGoalRepositoryTest {

    @Autowired
    private FinancialGoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save and find by id")
    void testSaveAndFindById() {
        User user = userRepository.save(new User(null, "user@example.com", false, 100));

        FinancialGoal goal = new FinancialGoal();
        goal.setUser(user);
        goal.setGoalType(GoalType.GROCERY);
        goal.setCustomAttrs("{\"target\":2000}");
        goal.setAchieved(false);

        FinancialGoal saved = goalRepository.save(goal);

        Optional<FinancialGoal> found = goalRepository.findById(saved.getGoalId());
        assertThat(found).isPresent();
        assertThat(found.get().getGoalType()).isEqualTo(GoalType.GROCERY);
        assertThat(found.get().getUser().getEmail()).isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("findAll returns all goals")
    void testFindAll() {
        User user = userRepository.save(new User(null, "two@example.com", false, 50));

        FinancialGoal g1 = new FinancialGoal(null, user, "{}", GoalType.INVESTMENT, false);
        FinancialGoal g2 = new FinancialGoal(null, user, "{}", GoalType.INSURANCE, true);

        goalRepository.saveAll(List.of(g1, g2));

        List<FinancialGoal> all = goalRepository.findAll();
        assertThat(all).hasSize(2);
        assertThat(all).extracting(FinancialGoal::getGoalType)
                       .containsExactlyInAnyOrder(GoalType.INVESTMENT, GoalType.INSURANCE);
    }

    @Test
    @DisplayName("deleteById removes the record")
    void testDeleteById() {
        User user = userRepository.save(new User(null, "delete@example.com", false, 20));
        FinancialGoal goal = new FinancialGoal(null, user, "{}", GoalType.GROCERY, false);
        FinancialGoal saved = goalRepository.save(goal);

        goalRepository.deleteById(saved.getGoalId());

        assertThat(goalRepository.findById(saved.getGoalId())).isEmpty();
    }
}
