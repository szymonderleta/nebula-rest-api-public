package pl.derleta.nebula.domain.mapper;

import org.junit.jupiter.api.Test;
import pl.derleta.nebula.controller.request.UserSettingsSoundRequest;
import pl.derleta.nebula.domain.entity.UserSettingsSoundEntity;
import pl.derleta.nebula.domain.model.UserSettingsSound;

import static org.junit.jupiter.api.Assertions.*;

class UserSettingsSoundMapperTest {

    @Test
    void toSetting_validEntity_returnsUserSettingsSound() {
        // Arrange
        UserSettingsSoundEntity entity = new UserSettingsSoundEntity();
        entity.setId(1000L);
        entity.setMuted(false);
        entity.setBattleCry(true);
        entity.setVolumeMaster(80);
        entity.setVolumeMusic(70);
        entity.setVolumeEffects(60);
        entity.setVolumeVoices(50);

        // Act
        UserSettingsSound result = UserSettingsSoundMapper.toSetting(entity);

        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.userId());
        assertFalse(result.muted());
        assertTrue(result.battleCry());
        assertEquals(80, result.volumeMaster());
        assertEquals(70, result.volumeMusic());
        assertEquals(60, result.volumeEffects());
        assertEquals(50, result.volumeVoices());
    }

    @Test
    void toSetting_nullEntity_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> UserSettingsSoundMapper.toSetting(null)
        );
    }

    @Test
    void toSetting_entityWithNullFields_returnsUserSettingsSoundWithDefaultFields() {
        // Arrange
        UserSettingsSoundEntity entity = new UserSettingsSoundEntity();
        entity.setId(1000L);
        // All other fields are null

        // Act
        UserSettingsSound result = UserSettingsSoundMapper.toSetting(entity);

        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.userId());
        assertFalse(result.muted());
        assertTrue(result.battleCry());
        assertEquals(100, result.volumeMaster());
        assertEquals(100, result.volumeMusic());
        assertEquals(100, result.volumeEffects());
        assertEquals(100, result.volumeVoices());
    }

    @Test
    void requestToSettings_validRequest_returnsUserSettingsSound() {
        // Arrange
        UserSettingsSoundRequest request = new UserSettingsSoundRequest(
                1000L, false, true, 80, 70, 60, 50
        );

        // Act
        UserSettingsSound result = UserSettingsSoundMapper.requestToSettings(request);

        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.userId());
        assertFalse(result.muted());
        assertTrue(result.battleCry());
        assertEquals(80, result.volumeMaster());
        assertEquals(70, result.volumeMusic());
        assertEquals(60, result.volumeEffects());
        assertEquals(50, result.volumeVoices());
    }

    @Test
    void requestToSettings_nullRequest_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> UserSettingsSoundMapper.requestToSettings(null)
        );
    }

    @Test
    void toEntity_validUserSettingsSound_returnsUserSettingsSoundEntity() {
        // Arrange
        UserSettingsSound sound = new UserSettingsSound(
                1000L, false, true, 80, 70, 60, 50
        );

        // Act
        UserSettingsSoundEntity result = UserSettingsSoundMapper.toEntity(sound);

        // Assert
        assertNotNull(result);
        assertEquals(1000L, result.getId());
        assertFalse(result.getMuted());
        assertTrue(result.getBattleCry());
        assertEquals(80, result.getVolumeMaster());
        assertEquals(70, result.getVolumeMusic());
        assertEquals(60, result.getVolumeEffects());
        assertEquals(50, result.getVolumeVoices());
    }

    @Test
    void toEntity_nullUserSettingsSound_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> UserSettingsSoundMapper.toEntity(null)
        );
    }

}
