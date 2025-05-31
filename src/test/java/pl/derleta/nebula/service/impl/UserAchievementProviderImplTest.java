package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.controller.request.UserAchievementFilterRequest;
import pl.derleta.nebula.domain.entity.AchievementEntity;
import pl.derleta.nebula.domain.entity.UserAchievementEntity;
import pl.derleta.nebula.domain.entity.UserEntity;
import pl.derleta.nebula.domain.entity.id.UserAchievementId;
import pl.derleta.nebula.domain.model.Achievement;
import pl.derleta.nebula.domain.model.UserAchievement;
import pl.derleta.nebula.repository.UserAchievementRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class UserAchievementProviderImplTest {

    @Mock
    private UserAchievementRepository repository;

    @InjectMocks
    private UserAchievementProviderImpl userAchievementProvider;

    private UserAchievementEntity testUserAchievementEntity;
    private UserAchievement testUserAchievement;
    private final Long userId = 123L;
    private final Integer achievementId = 456;

    @BeforeEach
    void setUp() {
        // Create a test user entity
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setLogin("testUser");
        userEntity.setEmail("test@example.com");

        // Create a test achievement entity
        AchievementEntity achievementEntity = new AchievementEntity();
        achievementEntity.setId(achievementId);
        achievementEntity.setName("Test Achievement");
        achievementEntity.setDescription("This is a test achievement");
        achievementEntity.setMinValue(0);
        achievementEntity.setMaxValue(100);
        achievementEntity.setIconUrl("https://example.com/icon.png");

        // Create test user achievement ID
        UserAchievementId userAchievementId = new UserAchievementId();
        userAchievementId.setUserId(userId);
        userAchievementId.setAchievementId(achievementId);

        // Create a test user achievement entity
        testUserAchievementEntity = new UserAchievementEntity();
        testUserAchievementEntity.setId(userAchievementId);
        testUserAchievementEntity.setUser(userEntity);
        testUserAchievementEntity.setAchievement(achievementEntity);
        testUserAchievementEntity.setProgress(7500); // 75.00%
        testUserAchievementEntity.setLevel(2);
        testUserAchievementEntity.setValue(75);

        // Create a test achievement model
        Achievement achievementModel = new Achievement(
                achievementId,
                "Test Achievement",
                0,
                100,
                "This is a test achievement",
                "https://example.com/icon.png",
                Collections.emptyList()
        );

        // Create a test user achievement model
        testUserAchievement = new UserAchievement(
                userId,
                achievementId,
                75,
                2,
                "75,00%",
                achievementModel
        );
    }

    @Test
    void get_shouldReturnUserAchievement_whenValidIdsProvided() {
        // Arrange
        when(repository.get(userId, achievementId)).thenReturn(testUserAchievementEntity);

        // Act
        UserAchievement result = userAchievementProvider.get(userId, achievementId);

        // Assert
        assertNotNull(result);
        assertEquals(testUserAchievement.userId(), result.userId());
        assertEquals(testUserAchievement.achievementId(), result.achievementId());
        assertEquals(testUserAchievement.value(), result.value());
        assertEquals(testUserAchievement.level(), result.level());
        assertEquals(testUserAchievement.progress(), result.progress());
        assertNotNull(result.achievement());
        assertEquals(testUserAchievement.achievement().id(), result.achievement().id());
        assertEquals(testUserAchievement.achievement().name(), result.achievement().name());
        assertEquals(testUserAchievement.achievement().description(), result.achievement().description());
        verify(repository, times(1)).get(userId, achievementId);
    }

    @Test
    void get_shouldReturnNull_whenUserAchievementNotFound() {
        // Arrange
        when(repository.get(userId, achievementId)).thenReturn(null);

        // Act
        UserAchievement result = userAchievementProvider.get(userId, achievementId);

        // Assert
        assertNull(result);
        verify(repository, times(1)).get(userId, achievementId);
    }

    @Test
    void getList_shouldReturnListOfUserAchievements_whenValidUserIdProvided() {
        // Arrange
        List<UserAchievementEntity> userAchievementEntities = Collections.singletonList(testUserAchievementEntity);
        when(repository.getList(userId)).thenReturn(userAchievementEntities);

        // Act
        List<UserAchievement> result = userAchievementProvider.getList(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUserAchievement.userId(), result.getFirst().userId());
        assertEquals(testUserAchievement.achievementId(), result.getFirst().achievementId());
        assertEquals(testUserAchievement.value(), result.getFirst().value());
        assertEquals(testUserAchievement.level(), result.getFirst().level());
        assertEquals(testUserAchievement.progress(), result.getFirst().progress());
        verify(repository, times(1)).getList(userId);
    }

    @Test
    void getList_shouldReturnEmptyList_whenNoUserAchievementsFound() {
        // Arrange
        when(repository.getList(userId)).thenReturn(Collections.emptyList());

        // Act
        List<UserAchievement> result = userAchievementProvider.getList(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).getList(userId);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getPage_shouldReturnPageOfUserAchievements_whenFilterRequestProvided() {
        // Arrange
        UserAchievementFilterRequest request = UserAchievementFilterRequest.builder()
                .page(0)
                .size(10)
                .sortBy("level")
                .sortOrder("DESC")
                .level(2)
                .filterType("ALL")
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "level"));
        List<UserAchievementEntity> userAchievementEntities = Collections.singletonList(testUserAchievementEntity);
        Page<UserAchievementEntity> entityPage = new PageImpl<>(userAchievementEntities, pageable, 1);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(entityPage);

        // Act
        Page<UserAchievement> result = userAchievementProvider.getPage(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testUserAchievement.userId(), result.getContent().getFirst().userId());
        assertEquals(testUserAchievement.achievementId(), result.getContent().getFirst().achievementId());
        assertEquals(testUserAchievement.value(), result.getContent().getFirst().value());
        assertEquals(testUserAchievement.level(), result.getContent().getFirst().level());
        assertEquals(testUserAchievement.progress(), result.getContent().getFirst().progress());
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void getPage_shouldReturnEmptyPage_whenNoUserAchievementsMatchFilter() {
        // Arrange
        UserAchievementFilterRequest request = UserAchievementFilterRequest.builder()
                .page(0)
                .size(10)
                .sortBy("level")
                .sortOrder("DESC")
                .level(3)
                .filterType("ALL")
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "level"));
        Page<UserAchievementEntity> entityPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(entityPage);

        // Act
        Page<UserAchievement> result = userAchievementProvider.getPage(request);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void getPage_shouldHandleMultiplePages() {
        // Arrange
        UserAchievementFilterRequest request = UserAchievementFilterRequest.builder()
                .page(1) // Second page
                .size(5)
                .sortBy("level")
                .sortOrder("DESC")
                .build();

        Pageable pageable = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "level"));

        List<UserAchievementEntity> userAchievementEntities = new ArrayList<>();
        userAchievementEntities.add(testUserAchievementEntity);

        Page<UserAchievementEntity> entityPage = new PageImpl<>(userAchievementEntities, pageable, 6);

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(entityPage);

        // Act
        Page<UserAchievement> result = userAchievementProvider.getPage(request);

        // Assert
        assertNotNull(result);
        assertEquals(6, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalPages());
        assertEquals(1, result.getNumber());
        verify(repository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

}
