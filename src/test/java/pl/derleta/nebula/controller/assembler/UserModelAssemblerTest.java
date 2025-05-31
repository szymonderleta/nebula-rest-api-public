package pl.derleta.nebula.controller.assembler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.hateoas.Link;
import pl.derleta.nebula.controller.UserController;
import pl.derleta.nebula.controller.mapper.NebulaUserApiMapper;
import pl.derleta.nebula.controller.response.NebulaUserResponse;
import pl.derleta.nebula.domain.model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

class UserModelAssemblerTest {

    NebulaUser user;
    NebulaUserResponse response;

    @BeforeEach
    void setUp() {
        Gender gender = new Gender(1, "Male");
        Region region = new Region(1, "Europe");
        Nationality nationality = new Nationality(1, "Polish", "POL", region);

        UserSettings settings = new UserSettings(
                1L,
                new UserSettingsGeneral(1L, new Theme(101, "Dark Mode")),
                new UserSettingsSound(1L, false, true, 80, 60, 70, 75)
        );

        user = new NebulaUser(
                1L,
                "johndoe",
                "john@nebula.com",
                "John",
                "Doe",
                25,
                Date.valueOf(LocalDate.of(1999, 4, 15)),
                gender,
                nationality,
                settings,
                List.of(
                        new Game(101, "Galaxy Raiders", true, "https://example.com/galaxy.png", "https://example.com/galaxy"),
                        new Game(102, "Nebula Quest", false, "https://example.com/nebula.png", "https://example.com/nebula")
                ),
                List.of()
        );

        response = NebulaUserResponse.builder()
                .id(user.id())
                .login(user.login())
                .email(user.email())
                .firstName(user.firstName())
                .lastName(user.lastName())
                .age(user.age())
                .birthDate(user.birthDate())
                .gender(user.gender())
                .nationality(user.nationality())
                .settings(user.settings())
                .games(user.games())
                .achievements(List.of())
                .build();
    }

    @Test
    void toModel_validUserAchievement_addsSelfLinkToResponse() {
        // Arrange
        UserModelAssembler assembler = new UserModelAssembler();

        try (MockedStatic<NebulaUserApiMapper> mockedStatic = Mockito.mockStatic(NebulaUserApiMapper.class)) {
            mockedStatic.when(() -> NebulaUserApiMapper.toResponse(user)).thenReturn(response);

            Link expectedLink = linkTo(UserController.class)
                    .slash(UserController.DEFAULT_PATH)
                    .slash(response.getId())
                    .withSelfRel();

            // Act
            NebulaUserResponse result = assembler.toModel(user);

            // Assert
            assertThat(result.getId()).isEqualTo(user.id());
            assertThat(result.getLogin()).isEqualTo(user.login());
            assertThat(result.getEmail()).isEqualTo(user.email());

            assertThat(result.getLinks()).containsExactly(expectedLink);

            // Verify static method
            mockedStatic.verify(() -> NebulaUserApiMapper.toResponse(user), times(1));
        }
    }

    @Test
    void toModel_validUserAchievement_mapsFieldsCorrectly() {
        // Arrange
        UserModelAssembler assembler = new UserModelAssembler();

        try (MockedStatic<NebulaUserApiMapper> mockedStatic = Mockito.mockStatic(NebulaUserApiMapper.class)) {
            mockedStatic.when(() -> NebulaUserApiMapper.toResponse(user)).thenReturn(response);

            // Act
            NebulaUserResponse result = assembler.toModel(user);

            // Assert
            assertThat(result.getId()).isEqualTo(user.id());
            assertThat(result.getLogin()).isEqualTo(user.login());
            assertThat(result.getEmail()).isEqualTo(user.email());
            assertThat(result.getFirstName()).isEqualTo(user.firstName());
            assertThat(result.getLastName()).isEqualTo(user.lastName());
            assertThat(result.getAge()).isEqualTo(user.age());
            assertThat(result.getBirthDate()).isEqualTo(user.birthDate());
            assertThat(result.getGender()).isEqualTo(user.gender());
            assertThat(result.getNationality()).isEqualTo(user.nationality());
            assertThat(result.getSettings()).isEqualTo(user.settings());
            assertThat(result.getGames().size()).isEqualTo(user.games().size());
            assertThat(result.getAchievements().size()).isEqualTo(user.achievements().size());

            // Verify static method
            mockedStatic.verify(() -> NebulaUserApiMapper.toResponse(user), times(1));
        }
    }

}
