package pl.derleta.nebula.controller.mapper;

import org.junit.jupiter.api.Test;
import pl.derleta.nebula.controller.response.UserSettingsResponse;
import pl.derleta.nebula.domain.model.Theme;
import pl.derleta.nebula.domain.model.UserSettings;
import pl.derleta.nebula.domain.model.UserSettingsGeneral;
import pl.derleta.nebula.domain.model.UserSettingsSound;

import static org.junit.jupiter.api.Assertions.*;

class UserSettingsApiMapperTest {

    @Test
    void toResponse_validUserSettings_returnsUserSettingsResponse() {
        // Arrange
        Theme theme = new Theme(1, "Dark");
        UserSettingsGeneral general = new UserSettingsGeneral(1000L, theme);
        UserSettingsSound sound = new UserSettingsSound(
                1000L, 
                false, 
                true, 
                80, 
                70, 
                60, 
                50
        );
        
        UserSettings userSettings = new UserSettings(1000L, general, sound);

        // Act
        UserSettingsResponse result = UserSettingsApiMapper.toResponse(userSettings);

        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.getUserId());
        assertEquals(general, result.getGeneral());
        assertEquals(sound, result.getSound());
    }

    @Test
    void toResponse_nullUserSettings_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> UserSettingsApiMapper.toResponse(null)
        );
    }

    @Test
    void toResponse_userSettingsWithNullFields_returnsUserSettingsResponseWithNullFields() {
        // Arrange
        UserSettings userSettings = new UserSettings(1000L, null, null);

        // Act
        UserSettingsResponse result = UserSettingsApiMapper.toResponse(userSettings);

        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.getUserId());
        assertNull(result.getGeneral());
        assertNull(result.getSound());
    }

}
