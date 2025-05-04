package pl.derleta.nebula.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.derleta.nebula.domain.entity.AchievementEntity;
import pl.derleta.nebula.domain.entity.UserAchievementEntity;
import pl.derleta.nebula.domain.entity.UserEntity;
import pl.derleta.nebula.domain.entity.id.UserAchievementId;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class UserAchievementRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Test
    @Transactional
    void findAll_withSpecification_shouldReturnFilteredResults() {
        // Arrange
        Long userId = 15L; // Using an existing user ID from the test database
        UserEntity user = userRepository.findById(userId).orElseThrow();
        
        AchievementEntity achievement = achievementRepository.findById(1).orElseThrow();
        
        UserAchievementId id = new UserAchievementId();
        id.setUserId(userId);
        id.setAchievementId(achievement.getId());
        
        UserAchievementEntity userAchievement = new UserAchievementEntity();
        userAchievement.setId(id);
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setProgress(5000); // 50%
        userAchievement.setLevel(1);
        userAchievement.setValue(100);
        
        userAchievementRepository.save(userAchievement);
        userAchievementRepository.flush();
        entityManager.clear();
        
        // Create a specification that filters by user ID
        Specification<UserAchievementEntity> spec = (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("id").get("userId"), userId);
        
        Pageable pageable = PageRequest.of(0, 10);
        
        // Act
        Page<UserAchievementEntity> result = userAchievementRepository.findAll(spec, pageable);
        
        // Assert
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should not be empty");
        assertTrue(result.getContent().stream()
                .allMatch(ua -> ua.getId().getUserId().equals(userId)),
                "All results should have the specified user ID");
    }

    @Test
    @Transactional
    void getList_existingUserId_shouldReturnUserAchievements() {
        // Arrange
        Long userId = 15L; // Using an existing user ID from the test database
        UserEntity user = userRepository.findById(userId).orElseThrow();
        
        AchievementEntity achievement1 = achievementRepository.findById(1).orElseThrow();
        AchievementEntity achievement2 = achievementRepository.findById(2).orElseThrow();
        
        // Create first user achievement
        UserAchievementId id1 = new UserAchievementId();
        id1.setUserId(userId);
        id1.setAchievementId(achievement1.getId());
        
        UserAchievementEntity userAchievement1 = new UserAchievementEntity();
        userAchievement1.setId(id1);
        userAchievement1.setUser(user);
        userAchievement1.setAchievement(achievement1);
        userAchievement1.setProgress(5000); // 50%
        userAchievement1.setLevel(1);
        userAchievement1.setValue(100);
        
        // Create second user achievement
        UserAchievementId id2 = new UserAchievementId();
        id2.setUserId(userId);
        id2.setAchievementId(achievement2.getId());
        
        UserAchievementEntity userAchievement2 = new UserAchievementEntity();
        userAchievement2.setId(id2);
        userAchievement2.setUser(user);
        userAchievement2.setAchievement(achievement2);
        userAchievement2.setProgress(7500); // 75%
        userAchievement2.setLevel(2);
        userAchievement2.setValue(200);
        
        userAchievementRepository.save(userAchievement1);
        userAchievementRepository.save(userAchievement2);
        userAchievementRepository.flush();
        entityManager.clear();
        
        // Act
        List<UserAchievementEntity> result = userAchievementRepository.getList(userId);
        
        // Assert
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should not be empty");
        assertTrue(result.stream()
                .allMatch(ua -> ua.getId().getUserId().equals(userId)),
                "All results should have the specified user ID");
        assertTrue(result.stream()
                .anyMatch(ua -> ua.getId().getAchievementId().equals(achievement1.getId())),
                "Results should contain the first achievement");
        assertTrue(result.stream()
                .anyMatch(ua -> ua.getId().getAchievementId().equals(achievement2.getId())),
                "Results should contain the second achievement");
    }

    @Test
    @Transactional
    void getList_nonExistingUserId_shouldReturnEmptyList() {
        // Arrange
        Long nonExistingUserId = 9999L;
        
        // Act
        List<UserAchievementEntity> result = userAchievementRepository.getList(nonExistingUserId);
        
        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty for non-existing user ID");
    }

    @Test
    @Transactional
    void get_existingUserIdAndAchievementId_shouldReturnUserAchievement() {
        // Arrange
        Long userId = 15L; // Using an existing user ID from the test database
        UserEntity user = userRepository.findById(userId).orElseThrow();
        
        AchievementEntity achievement = achievementRepository.findById(1).orElseThrow();
        
        UserAchievementId id = new UserAchievementId();
        id.setUserId(userId);
        id.setAchievementId(achievement.getId());
        
        UserAchievementEntity userAchievement = new UserAchievementEntity();
        userAchievement.setId(id);
        userAchievement.setUser(user);
        userAchievement.setAchievement(achievement);
        userAchievement.setProgress(5000); // 50%
        userAchievement.setLevel(1);
        userAchievement.setValue(100);
        
        userAchievementRepository.save(userAchievement);
        userAchievementRepository.flush();
        entityManager.clear();
        
        // Act
        UserAchievementEntity result = userAchievementRepository.get(userId, achievement.getId());
        
        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(userId, result.getId().getUserId(), "User ID should match");
        assertEquals(achievement.getId(), result.getId().getAchievementId(), "Achievement ID should match");
        assertEquals(5000, result.getProgress(), "Progress should match");
        assertEquals(1, result.getLevel(), "Level should match");
        assertEquals(100, result.getValue(), "Value should match");
    }

    @Test
    @Transactional
    void get_nonExistingUserIdOrAchievementId_shouldReturnNull() {
        // Arrange
        Long nonExistingUserId = 9999L;
        Integer existingAchievementId = 1;
        
        Long existingUserId = 15L;
        Integer nonExistingAchievementId = 9999;
        
        // Act
        UserAchievementEntity result1 = userAchievementRepository.get(nonExistingUserId, existingAchievementId);
        UserAchievementEntity result2 = userAchievementRepository.get(existingUserId, nonExistingAchievementId);
        
        // Assert
        assertNull(result1, "Result should be null for non-existing user ID");
        assertNull(result2, "Result should be null for non-existing achievement ID");
    }

}
