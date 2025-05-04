package pl.derleta.nebula.domain.mapper;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.derleta.nebula.controller.request.UserSettingsGeneralRequest;
import pl.derleta.nebula.domain.entity.ThemeEntity;
import pl.derleta.nebula.domain.entity.UserSettingsGeneralEntity;
import pl.derleta.nebula.domain.model.Theme;
import pl.derleta.nebula.domain.model.UserSettingsGeneral;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class UserSettingsGeneralMapperTest {

    @Test
    void toSetting_validEntity_returnsUserSettingsGeneral() {
        // Arrange
        UserSettingsGeneralEntity entity = new UserSettingsGeneralEntity();
        entity.setId(1000L);

        ThemeEntity themeEntity = new ThemeEntity();
        themeEntity.setId(1);
        themeEntity.setName("Dark");
        entity.setTheme(themeEntity);

        Theme theme = new Theme(1, "Dark");

        try (MockedStatic<ThemeMapper> mockedStatic = Mockito.mockStatic(ThemeMapper.class)) {
            mockedStatic.when(() -> ThemeMapper.toTheme(any()))
                    .thenReturn(theme);

            // Act
            UserSettingsGeneral result = UserSettingsGeneralMapper.toSetting(entity);

            // Assert
            assertNotNull(result);
            assertEquals(1000L, result.userId());
            assertEquals(theme, result.theme());

            mockedStatic.verify(() -> ThemeMapper.toTheme(themeEntity));
        }
    }

    @Test
    void toSetting_nullEntity_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> UserSettingsGeneralMapper.toSetting(null)
        );
    }

    @Test
    void toSetting_entityWithNullTheme_returnsUserSettingsGeneralWithNullTheme() {
        // Arrange
        UserSettingsGeneralEntity entity = new UserSettingsGeneralEntity();
        entity.setId(1000L);
        // Theme is null

        try (MockedStatic<ThemeMapper> mockedStatic = Mockito.mockStatic(ThemeMapper.class)) {
            mockedStatic.when(() -> ThemeMapper.toTheme(any()))
                    .thenReturn(null);

            // Act
            UserSettingsGeneral result = UserSettingsGeneralMapper.toSetting(entity);

            // Assert
            assertNotNull(result);
            assertEquals(1000L, result.userId());
            assertNull(result.theme());

            mockedStatic.verify(() -> ThemeMapper.toTheme(null));
        }
    }

    @Test
    void requestToSettings_validRequest_returnsUserSettingsGeneral() {
        // Arrange
        Theme theme = new Theme(1, "Dark");
        UserSettingsGeneralRequest request = new UserSettingsGeneralRequest(1000L, theme);

        // Act
        UserSettingsGeneral result = UserSettingsGeneralMapper.requestToSettings(request);

        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.userId());
        assertEquals(theme, result.theme());
    }

    @Test
    void requestToSettings_nullRequest_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> UserSettingsGeneralMapper.requestToSettings(null)
        );
    }

    @Test
    void toEntity_validUserSettingsGeneral_returnsUserSettingsGeneralEntity() {
        // Arrange
        Theme theme = new Theme(1, "Dark");
        UserSettingsGeneral userSettingsGeneral = new UserSettingsGeneral(1000L, theme);

        ThemeEntity themeEntity = new ThemeEntity();
        themeEntity.setId(1);
        themeEntity.setName("Dark");

        try (MockedStatic<ThemeMapper> mockedStatic = Mockito.mockStatic(ThemeMapper.class)) {
            mockedStatic.when(() -> ThemeMapper.toEntity(any()))
                    .thenReturn(themeEntity);

            // Act
            UserSettingsGeneralEntity result = UserSettingsGeneralMapper.toEntity(userSettingsGeneral);

            // Assert
            assertNotNull(result);
            assertEquals(1000L, result.getId());
            assertEquals(themeEntity, result.getTheme());

            mockedStatic.verify(() -> ThemeMapper.toEntity(theme));
        }
    }

    @Test
    void toEntity_nullUserSettingsGeneral_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> UserSettingsGeneralMapper.toEntity(null)
        );
    }

    @Test
    void toEntity_userSettingsGeneralWithNullTheme_returnsUserSettingsGeneralEntityWithNullTheme() {
        // Arrange
        UserSettingsGeneral userSettingsGeneral = new UserSettingsGeneral(1000L, null);

        try (MockedStatic<ThemeMapper> mockedStatic = Mockito.mockStatic(ThemeMapper.class)) {
            mockedStatic.when(() -> ThemeMapper.toEntity(any()))
                    .thenReturn(null);

            // Act
            UserSettingsGeneralEntity result = UserSettingsGeneralMapper.toEntity(userSettingsGeneral);

            // Assert
            assertNotNull(result);
            assertEquals(1000L, result.getId());
            assertNull(result.getTheme());

            mockedStatic.verify(() -> ThemeMapper.toEntity(null));
        }
    }

}
