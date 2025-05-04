package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.controller.request.ProfileUpdateRequest;
import pl.derleta.nebula.controller.request.Request;
import pl.derleta.nebula.domain.entity.GenderEntity;
import pl.derleta.nebula.domain.entity.NationalityEntity;
import pl.derleta.nebula.domain.entity.ThemeEntity;
import pl.derleta.nebula.domain.entity.UserEntity;
import pl.derleta.nebula.domain.entity.UserSettingsEntity;
import pl.derleta.nebula.domain.entity.UserSettingsGeneralEntity;
import pl.derleta.nebula.domain.entity.UserSettingsSoundEntity;
import pl.derleta.nebula.domain.mapper.NebulaUserMapper;
import pl.derleta.nebula.domain.mapper.UserSettingsMapper;
import pl.derleta.nebula.domain.model.Gender;
import pl.derleta.nebula.domain.model.Nationality;
import pl.derleta.nebula.domain.model.NebulaUser;
import pl.derleta.nebula.domain.model.Region;
import pl.derleta.nebula.domain.model.Theme;
import pl.derleta.nebula.domain.model.UserSettings;
import pl.derleta.nebula.domain.model.UserSettingsGeneral;
import pl.derleta.nebula.domain.model.UserSettingsSound;
import pl.derleta.nebula.repository.UserRepository;
import pl.derleta.nebula.repository.UserSettingsRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class UserUpdaterImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSettingsRepository userSettingsRepository;

    @InjectMocks
    private UserUpdaterImpl userUpdater;

    private final Long userId = 123L;
    private UserEntity testUserEntity;
    private NebulaUser testNebulaUser;
    private ProfileUpdateRequest testProfileUpdateRequest;
    private UserSettings testUserSettings;
    private UserSettingsEntity testUserSettingsEntity;

    @BeforeEach
    void setUp() {
        // Create a test gender entity
        GenderEntity genderEntity = new GenderEntity();
        genderEntity.setId(1);
        genderEntity.setName("Male");

        // Create a test region entity
        Region region = new Region(1, "Europe");

        // Create a test nationality entity
        NationalityEntity nationalityEntity = new NationalityEntity();
        nationalityEntity.setId(1);
        nationalityEntity.setName("United States");
        nationalityEntity.setCode("USA");

        // Create a test user entity
        testUserEntity = new UserEntity();
        testUserEntity.setId(userId);
        testUserEntity.setLogin("testUser");
        testUserEntity.setEmail("test@example.com");
        testUserEntity.setFirstName("John");
        testUserEntity.setLastName("Doe");
        testUserEntity.setAge(30);
        testUserEntity.setBirthDate(Date.valueOf(LocalDate.of(1993, 1, 1)));
        testUserEntity.setGender(genderEntity);
        testUserEntity.setNationality(nationalityEntity);
        testUserEntity.setGames(Collections.emptyList());
        testUserEntity.setAchievements(Collections.emptyList());

        // Create test NebulaUser
        Gender gender = new Gender(1, "Male");
        Nationality nationality = new Nationality(1, "United States", "USA", region);
        
        testNebulaUser = new NebulaUser(
                userId,
                "testUser",
                "test@example.com",
                "John",
                "Doe",
                30,
                Date.valueOf(LocalDate.of(1993, 1, 1)),
                gender,
                nationality,
                null,
                Collections.emptyList(),
                Collections.emptyList()
        );

        // Create a test ProfileUpdateRequest
        testProfileUpdateRequest = ProfileUpdateRequest.builder()
                .userId(userId)
                .firstName("John")
                .lastName("Doe")
                .birthdate(Date.valueOf(LocalDate.of(1993, 1, 1)))
                .nationalityId(1)
                .genderId(1)
                .build();

        // Create a test Theme
        Theme theme = new Theme(1, "Dark");
        ThemeEntity themeEntity = new ThemeEntity();
        themeEntity.setId(1);
        themeEntity.setName("Dark");

        // Create test UserSettingsGeneral
        UserSettingsGeneral userSettingsGeneral = new UserSettingsGeneral(userId, theme);
        UserSettingsGeneralEntity userSettingsGeneralEntity = new UserSettingsGeneralEntity();
        userSettingsGeneralEntity.setId(userId);
        userSettingsGeneralEntity.setTheme(themeEntity);

        // Create test UserSettingsSound
        UserSettingsSound userSettingsSound = new UserSettingsSound(
                userId, false, true, 80, 70, 60, 50);
        UserSettingsSoundEntity userSettingsSoundEntity = new UserSettingsSoundEntity();
        userSettingsSoundEntity.setId(userId);
        userSettingsSoundEntity.setMuted(false);
        userSettingsSoundEntity.setBattleCry(true);
        userSettingsSoundEntity.setVolumeMaster(80);
        userSettingsSoundEntity.setVolumeMusic(70);
        userSettingsSoundEntity.setVolumeEffects(60);
        userSettingsSoundEntity.setVolumeVoices(50);

        // Create test UserSettings
        testUserSettings = new UserSettings(userId, userSettingsGeneral, userSettingsSound);

        // Create a test UserSettingsEntity
        testUserSettingsEntity = new UserSettingsEntity();
        testUserSettingsEntity.setId(userId);
        testUserSettingsEntity.setGeneral(userSettingsGeneralEntity);
        testUserSettingsEntity.setSound(userSettingsSoundEntity);
    }

    @Test
    void updateProfile_shouldReturnUpdatedUser_whenValidProfileUpdateRequestProvided() {
        // Arrange
        when(userRepository.getReferenceById(userId)).thenReturn(testUserEntity);
        doNothing().when(userRepository).updateUserDetails(
                eq(userId), eq("John"), eq("Doe"), 
                eq(Date.valueOf(LocalDate.of(1993, 1, 1))), eq(1), eq(1));

        try (MockedStatic<NebulaUserMapper> mockedMapper = Mockito.mockStatic(NebulaUserMapper.class)) {
            mockedMapper.when(() -> NebulaUserMapper.toUser(testUserEntity)).thenReturn(testNebulaUser);
            
            // Act
            NebulaUser result = userUpdater.updateProfile(testProfileUpdateRequest);
            
            // Assert
            assertNotNull(result);
            assertEquals(testNebulaUser.id(), result.id());
            assertEquals(testNebulaUser.login(), result.login());
            assertEquals(testNebulaUser.email(), result.email());
            assertEquals(testNebulaUser.firstName(), result.firstName());
            assertEquals(testNebulaUser.lastName(), result.lastName());
            
            verify(userRepository, times(1)).updateUserDetails(
                    eq(userId), eq("John"), eq("Doe"), 
                    eq(Date.valueOf(LocalDate.of(1993, 1, 1))), eq(1), eq(1));
            verify(userRepository, times(1)).getReferenceById(userId);
            mockedMapper.verify(() -> NebulaUserMapper.toUser(testUserEntity), times(1));
        }
    }

    @Test
    void updateProfile_shouldReturnNull_whenInvalidRequestTypeProvided() {
        // Arrange
        Request invalidRequest = new Request() {};
        
        // Act
        NebulaUser result = userUpdater.updateProfile(invalidRequest);
        
        // Assert
        assertNull(result);
        verify(userRepository, never()).updateUserDetails(
                anyLong(), anyString(), anyString(), any(Date.class), anyInt(), anyInt());
        verify(userRepository, never()).getReferenceById(anyLong());
    }

    @Test
    void updateSettings_shouldReturnUpdatedSettings_whenValidSettingsProvided() {
        // Arrange
        when(userSettingsRepository.save(any(UserSettingsEntity.class))).thenReturn(testUserSettingsEntity);
        doNothing().when(userRepository).updateUserUpdatedAt(userId);
        
        try (MockedStatic<UserSettingsMapper> mockedMapper = Mockito.mockStatic(UserSettingsMapper.class)) {
            mockedMapper.when(() -> UserSettingsMapper.toEntity(testUserSettings)).thenReturn(testUserSettingsEntity);
            mockedMapper.when(() -> UserSettingsMapper.toSetting(testUserSettingsEntity)).thenReturn(testUserSettings);
            
            // Act
            UserSettings result = userUpdater.updateSettings(testUserSettings);
            
            // Assert
            assertNotNull(result);
            assertEquals(testUserSettings.userId(), result.userId());
            assertEquals(testUserSettings.general().userId(), result.general().userId());
            assertEquals(testUserSettings.sound().userId(), result.sound().userId());
            
            verify(userSettingsRepository, times(1)).save(testUserSettingsEntity);
            verify(userRepository, times(1)).updateUserUpdatedAt(userId);
            mockedMapper.verify(() -> UserSettingsMapper.toEntity(testUserSettings), times(1));
            mockedMapper.verify(() -> UserSettingsMapper.toSetting(testUserSettingsEntity), times(1));
        }
    }

    @Test
    void updateSettings_shouldThrowIllegalArgumentException_whenUserIdsDoNotMatch() {
        // Arrange
        UserSettingsGeneral generalWithDifferentId = new UserSettingsGeneral(456L, new Theme(1, "Dark"));
        UserSettingsSound soundWithSameId = new UserSettingsSound(userId, false, true, 80, 70, 60, 50);
        UserSettings settingsWithMismatchedIds = new UserSettings(userId, generalWithDifferentId, soundWithSameId);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userUpdater.updateSettings(settingsWithMismatchedIds));
        
        assertEquals("User IDs in settings do not match!", exception.getMessage());
        verify(userSettingsRepository, never()).save(any(UserSettingsEntity.class));
        verify(userRepository, never()).updateUserUpdatedAt(anyLong());
    }

    @Test
    void updateSettings_shouldThrowIllegalArgumentException_whenSoundUserIdDoesNotMatch() {
        // Arrange
        UserSettingsGeneral generalWithSameId = new UserSettingsGeneral(userId, new Theme(1, "Dark"));
        UserSettingsSound soundWithDifferentId = new UserSettingsSound(789L, false, true, 80, 70, 60, 50);
        UserSettings settingsWithMismatchedIds = new UserSettings(userId, generalWithSameId, soundWithDifferentId);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userUpdater.updateSettings(settingsWithMismatchedIds));
        
        assertEquals("User IDs in settings do not match!", exception.getMessage());
        verify(userSettingsRepository, never()).save(any(UserSettingsEntity.class));
        verify(userRepository, never()).updateUserUpdatedAt(anyLong());
    }

}
