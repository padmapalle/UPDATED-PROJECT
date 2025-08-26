package com.financialapp.repository;

import com.financialapp.entity.RewardCatalog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=Oracle;DB_CLOSE_DELAY=-1"
})
class RewardCatalogRepositoryTest {

    @Autowired
    private RewardCatalogRepository rewardCatalogRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByCatalogItemIdAndActiveTrue_found() {
        // Use native SQL to insert to avoid Hibernate merge issues
        entityManager.getEntityManager().createNativeQuery(
                "INSERT INTO reward_catalog (catalog_item_id, active, name) VALUES (1001, true, 'Test Reward')"
        ).executeUpdate();
        entityManager.flush();
        entityManager.clear();

        Optional<RewardCatalog> result = rewardCatalogRepository.findByCatalogItemIdAndActiveTrue(1001);

        assertThat(result).isPresent();
        assertThat(result.get().getCatalogItemId()).isEqualTo(1001);
        assertThat(result.get().getActive()).isTrue();
    }

    @Test
    void testFindByCatalogItemIdAndActiveTrue_notFound_dueToInactive() {
        // Use native SQL to insert
        entityManager.getEntityManager().createNativeQuery(
                "INSERT INTO reward_catalog (catalog_item_id, active, name) VALUES (2002, false, 'Inactive Reward')"
        ).executeUpdate();
        entityManager.flush();
        entityManager.clear();

        Optional<RewardCatalog> result = rewardCatalogRepository.findByCatalogItemIdAndActiveTrue(2002);

        assertThat(result).isNotPresent();
    }

    @Test
    void testFindByCatalogItemIdAndActiveTrue_notFound_dueToMissingItem() {
        Optional<RewardCatalog> result = rewardCatalogRepository.findByCatalogItemIdAndActiveTrue(9999);
        assertThat(result).isNotPresent();
    }
}