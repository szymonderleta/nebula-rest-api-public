package pl.derleta.nebula.controller.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.derleta.nebula.controller.response.NebulaUserResponse;
import pl.derleta.nebula.domain.mapper.NebulaUserAchievementMapper;
import pl.derleta.nebula.domain.model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class NebulaUserApiMapperTest {

    @Test
    void toResponse_validNebulaUser_returnsNebulaUserResponse() {
        // Arrange
        Gender gender = new Gender(1, "Male");
        Region region = new Region(1, "Europe");
        Nationality nationality = new Nationality(1, "Polish", "PL", region);
        UserSettings settings = new UserSettings(1000L, null, null);

        Game game1 = new Game(1, "Game 1", true, "https://example.com/icon1.png", "https://example.com/game1");
        Game game2 = new Game(2, "Game 2", false, "https://example.com/icon2.png", "https://example.com/game2");
        List<Game> games = Arrays.asList(game1, game2);

        Achievement achievement = new Achievement(1, "Achievement 1", 0, 100, "Description 1", "https://example.com/icon.png", null);
        UserAchievement userAchievement = new UserAchievement(1000L, 1, 30, 1, "30%", achievement);
        List<UserAchievement> achievements = Collections.singletonList(userAchievement);

        NebulaUser nebulaUser = new NebulaUser(
                1000L, "testuser", "test@example.com", "John", "Doe",
                30, Date.valueOf(LocalDate.of(1993, 1, 1)), gender, nationality,
                settings, games, achievements
        );

        NebulaUserAchievement nebulaUserAchievement = new NebulaUserAchievement(
                1, "Achievement 1", 0, 100, 30, 1, "30%", "Description 1", "https://example.com/icon.png", null
        );
        List<NebulaUserAchievement> nebulaUserAchievements = Collections.singletonList(nebulaUserAchievement);

        try (MockedStatic<NebulaUserAchievementMapper> mockedStatic = Mockito.mockStatic(NebulaUserAchievementMapper.class)) {
            mockedStatic.when(() -> NebulaUserAchievementMapper.toList(any()))
                    .thenReturn(nebulaUserAchievements);

            // Act
            NebulaUserResponse result = NebulaUserApiMapper.toResponse(nebulaUser);

            // Assert
            assertNotNull(result);
            assertEquals(1000L, result.getId());
            assertEquals("testuser", result.getLogin());
            assertEquals("test@example.com", result.getEmail());
            assertEquals("John", result.getFirstName());
            assertEquals("Doe", result.getLastName());
            assertEquals(30, result.getAge());
            assertEquals(Date.valueOf(LocalDate.of(1993, 1, 1)), result.getBirthDate());
            assertEquals(gender, result.getGender());
            assertEquals(nationality, result.getNationality());
            assertEquals(settings, result.getSettings());
            assertEquals(games, result.getGames());
            assertEquals(nebulaUserAchievements, result.getAchievements());

            // Verify that NebulaUserAchievementMapper.toList was called with the correct argument
            mockedStatic.verify(() -> NebulaUserAchievementMapper.toList(achievements));
        }
    }

    @Test
    void toResponse_nullNebulaUser_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> NebulaUserApiMapper.toResponse(null)
        );
    }

}
