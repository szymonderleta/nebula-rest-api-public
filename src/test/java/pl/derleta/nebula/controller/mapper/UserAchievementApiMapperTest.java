package pl.derleta.nebula.controller.mapper;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.derleta.nebula.controller.response.UserAchievementResponse;
import pl.derleta.nebula.domain.model.Achievement;
import pl.derleta.nebula.domain.model.AchievementLevel;
import pl.derleta.nebula.domain.model.UserAchievement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserAchievementApiMapperTest {

    @Test
    void toResponse_validUserAchievement_returnsUserAchievementResponse() {
        // Arrange
        List<AchievementLevel> levels = Arrays.asList(
                new AchievementLevel(1, 25),
                new AchievementLevel(2, 50)
        );

        Achievement achievement = new Achievement(
                1,
                "Achievement 1",
                0,
                100,
                "Description 1",
                "https://example.com/icon1.png",
                levels
        );

        UserAchievement userAchievement = new UserAchievement(
                1000L,
                1,
                30,
                1,
                "30%",
                achievement
        );

        // Act
        UserAchievementResponse result = UserAchievementApiMapper.toResponse(userAchievement);

        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.getUserId());
        assertEquals(1, result.getAchievementId());
        assertEquals(30, result.getValue());
        assertEquals(1, result.getLevel());
        assertEquals("30%", result.getProgress());
        assertEquals(achievement, result.getAchievement());
    }

    @Test
    void toResponse_nullUserAchievement_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> UserAchievementApiMapper.toResponse(null)
        );
    }

    @Test
    void toPageResponse_validPage_returnsPageOfUserAchievementResponses() {
        // Arrange
        List<AchievementLevel> levels = Arrays.asList(
                new AchievementLevel(1, 25),
                new AchievementLevel(2, 50)
        );

        Achievement achievement1 = new Achievement(
                1,
                "Achievement 1",
                0,
                100,
                "Description 1",
                "https://example.com/icon1.png",
                levels
        );

        Achievement achievement2 = new Achievement(
                2,
                "Achievement 2",
                0,
                200,
                "Description 2",
                "https://example.com/icon2.png",
                levels
        );

        Page<UserAchievement> page = getUserAchievements(achievement1, achievement2);

        // Act
        Page<UserAchievementResponse> result = UserAchievementApiMapper.toPageResponse(page);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());

        List<UserAchievementResponse> content = result.getContent();
        assertEquals(2, content.size());

        UserAchievementResponse response1 = content.getFirst();
        assertEquals(1000L, response1.getUserId());
        assertEquals(1, response1.getAchievementId());
        assertEquals(30, response1.getValue());
        assertEquals(1, response1.getLevel());
        assertEquals("30%", response1.getProgress());
        assertEquals(achievement1, response1.getAchievement());

        UserAchievementResponse response2 = content.get(1);
        assertEquals(1000L, response2.getUserId());
        assertEquals(2, response2.getAchievementId());
        assertEquals(60, response2.getValue());
        assertEquals(2, response2.getLevel());
        assertEquals("60%", response2.getProgress());
        assertEquals(achievement2, response2.getAchievement());
    }

    @NotNull
    private static Page<UserAchievement> getUserAchievements(Achievement achievement1, Achievement achievement2) {
        UserAchievement userAchievement1 = new UserAchievement(
                1000L,
                1,
                30,
                1,
                "30%",
                achievement1
        );

        UserAchievement userAchievement2 = new UserAchievement(
                1000L,
                2,
                60,
                2,
                "60%",
                achievement2
        );

        List<UserAchievement> userAchievements = Arrays.asList(userAchievement1, userAchievement2);
        Pageable pageable = PageRequest.of(0, 10);
        return new PageImpl<>(userAchievements, pageable, userAchievements.size());
    }

    @Test
    void toPageResponse_emptyPage_returnsEmptyPage() {
        // Arrange
        List<UserAchievement> userAchievements = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserAchievement> page = new PageImpl<>(userAchievements, pageable, 0);

        // Act
        Page<UserAchievementResponse> result = UserAchievementApiMapper.toPageResponse(page);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void toPageResponse_nullPage_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> UserAchievementApiMapper.toPageResponse(null)
        );
    }

}
