package pl.derleta.nebula.controller.assembler;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.hateoas.Link;
import pl.derleta.nebula.controller.UserAchievementController;
import pl.derleta.nebula.controller.mapper.UserAchievementApiMapper;
import pl.derleta.nebula.controller.response.UserAchievementResponse;
import pl.derleta.nebula.domain.model.Achievement;
import pl.derleta.nebula.domain.model.UserAchievement;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

class UserAchievementModelAssemblerTest {

    @Test
    void toModel_validUserAchievement_addsSelfLinkToResponse() {
        // Arrange
        UserAchievementModelAssembler assembler = new UserAchievementModelAssembler();
        Achievement achievement = new Achievement(3, "Gamewinner", 0, 100,
                "description", "https://example.com/icon.png", List.of());
        UserAchievement entity = new UserAchievement(123L, 1, 30, 3,
                "In progress", achievement);

        UserAchievementResponse mappedResponse = UserAchievementResponse.builder()
                .userId(entity.userId())
                .achievementId(entity.achievementId())
                .value(entity.value())
                .level(entity.level())
                .progress(entity.progress())
                .achievement(entity.achievement())
                .build();

        try (MockedStatic<UserAchievementApiMapper> mockedStatic = Mockito.mockStatic(UserAchievementApiMapper.class)) {
            mockedStatic.when(() -> UserAchievementApiMapper.toResponse(entity)).thenReturn(mappedResponse);

            Link expectedLink = linkTo(UserAchievementController.class)
                    .slash(UserAchievementController.DEFAULT_PATH)
                    .slash(mappedResponse.getUserId())
                    .slash(mappedResponse.getAchievementId())
                    .withSelfRel();

            // Act
            UserAchievementResponse result = assembler.toModel(entity);

            // Assert
            assertThat(result.getUserId()).isEqualTo(entity.userId());
            assertThat(result.getAchievementId()).isEqualTo(entity.achievementId());

            assertThat(result.getLinks()).containsExactly(expectedLink);

            // Verify static method
            mockedStatic.verify(() -> UserAchievementApiMapper.toResponse(entity), times(1));
        }
    }

    @Test
    void toModel_validUserAchievement_mapsFieldsCorrectly() {
        // Arrange
        UserAchievementModelAssembler assembler = new UserAchievementModelAssembler();
        Achievement achievement = new Achievement(3, "Gamewinner", 0, 100,
                "description", "https://example.com/icon.png", List.of());
        UserAchievement entity = new UserAchievement(123L, 1, 30, 3,
                "In progress", achievement);

        UserAchievementResponse mappedResponse = UserAchievementResponse.builder()
                .userId(entity.userId())
                .achievementId(entity.achievementId())
                .value(entity.value())
                .level(entity.level())
                .progress(entity.progress())
                .achievement(entity.achievement())
                .build();

        try (MockedStatic<UserAchievementApiMapper> mockedStatic = Mockito.mockStatic(UserAchievementApiMapper.class)) {
            mockedStatic.when(() -> UserAchievementApiMapper.toResponse(entity)).thenReturn(mappedResponse);

            // Act
            UserAchievementResponse result = assembler.toModel(entity);

            // Assert
            assertThat(result.getUserId()).isEqualTo(entity.userId());
            assertThat(result.getAchievementId()).isEqualTo(entity.achievementId());
            assertThat(result.getValue()).isEqualTo(entity.value());
            assertThat(result.getLevel()).isEqualTo(entity.level());
            assertThat(result.getProgress()).isEqualTo(entity.progress());
            assertThat(result.getAchievement()).isEqualTo(entity.achievement());

            // Verify static method
            mockedStatic.verify(() -> UserAchievementApiMapper.toResponse(entity), times(1));
        }
    }

}
