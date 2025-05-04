package pl.derleta.nebula.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.derleta.nebula.controller.assembler.UserModelAssembler;
import pl.derleta.nebula.controller.mapper.UserSettingsApiMapper;
import pl.derleta.nebula.controller.request.ProfileUpdateRequest;
import pl.derleta.nebula.controller.request.UserSettingsRequest;
import pl.derleta.nebula.controller.response.NebulaUserResponse;
import pl.derleta.nebula.controller.response.Response;
import pl.derleta.nebula.controller.response.UserSettingsResponse;
import pl.derleta.nebula.domain.mapper.UserSettingsMapper;
import pl.derleta.nebula.domain.model.*;
import pl.derleta.nebula.service.TokenProvider;
import pl.derleta.nebula.service.UserProvider;
import pl.derleta.nebula.service.UserUpdater;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserProvider userProvider;

    @Mock
    private UserUpdater userUpdater;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private UserModelAssembler userModelAssembler;

    @InjectMocks
    private UserController userController;

    private final String validToken = "valid-token";
    private final Long userId = 1000L;
    private NebulaUser nebulaUser;
    private NebulaUserResponse nebulaUserResponse;
    private ProfileUpdateRequest profileUpdateRequest;
    private UserSettingsRequest userSettingsRequest;
    private UserSettings userSettings;
    private UserSettingsResponse userSettingsResponse;

    @BeforeEach
    void setUp() {
        // Set up NebulaUser
        Gender gender = new Gender(1, "Male");
        Region region = new Region(1, "Europe");
        Nationality nationality = new Nationality(1, "Polish", "PL", region);
        Theme theme = new Theme(1, "Dark");

        UserSettingsGeneral general = new UserSettingsGeneral(userId, theme);
        UserSettingsSound sound = new UserSettingsSound(
                userId, 
                false, 
                true, 
                80, 
                70, 
                60, 
                50
        );

        userSettings = new UserSettings(userId, general, sound);

        nebulaUser = new NebulaUser(
                userId,
                "testuser",
                "test@example.com",
                "John",
                "Doe",
                30,
                Date.valueOf(LocalDate.of(1993, 1, 1)),
                gender,
                nationality,
                userSettings,
                Collections.emptyList(),
                Collections.emptyList()
        );

        nebulaUserResponse = NebulaUserResponse.builder()
                .id(userId)
                .login("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .birthDate(Date.valueOf(LocalDate.of(1993, 1, 1)))
                .gender(gender)
                .nationality(nationality)
                .settings(userSettings)
                .games(Collections.emptyList())
                .achievements(Collections.emptyList())
                .build();

        // Set up ProfileUpdateRequest
        profileUpdateRequest = ProfileUpdateRequest.builder()
                .userId(userId)
                .firstName("John")
                .lastName("Doe")
                .birthdate(Date.valueOf(LocalDate.of(1993, 1, 1)))
                .nationalityId(1)
                .genderId(1)
                .build();

        // Set up UserSettingsRequest
        userSettingsRequest = new UserSettingsRequest(
                userId,
                null,
                null
        );

        // Set up UserSettingsResponse
        userSettingsResponse = UserSettingsResponse.builder()
                .userId(userId)
                .general(general)
                .sound(sound)
                .build();
    }

    @Test
    void getUserData_validToken_returnsNebulaUserResponse() {
        // Arrange
        when(tokenProvider.isValid(validToken)).thenReturn(true);
        when(tokenProvider.getUserId(validToken)).thenReturn(userId);
        when(userProvider.get(userId)).thenReturn(nebulaUser);
        when(userModelAssembler.toModel(nebulaUser)).thenReturn(nebulaUserResponse);

        // Act
        ResponseEntity<NebulaUserResponse> response = userController.getUserData(validToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nebulaUserResponse, response.getBody());
        verify(tokenProvider, times(1)).isValid(validToken);
        verify(tokenProvider, times(1)).getUserId(validToken);
        verify(userProvider, times(1)).get(userId);
        verify(userModelAssembler, times(1)).toModel(nebulaUser);
    }

    @Test
    void getUserData_invalidToken_returnsForbidden() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<NebulaUserResponse> response = userController.getUserData(invalidToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(invalidToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(userProvider, never()).get(any());
        verify(userModelAssembler, never()).toModel(any());
    }

    @Test
    void updateUserProfile_validTokenAndMatchingUserId_returnsUpdatedProfile() {
        // Arrange
        when(tokenProvider.isValid(validToken, userId)).thenReturn(true);
        when(userUpdater.updateProfile(profileUpdateRequest)).thenReturn(nebulaUser);
        when(userModelAssembler.toModel(nebulaUser)).thenReturn(nebulaUserResponse);

        // Act
        ResponseEntity<Response> response = userController.updateUserProfile(validToken, profileUpdateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nebulaUserResponse, response.getBody());
        verify(tokenProvider, times(1)).isValid(validToken, userId);
        verify(userUpdater, times(1)).updateProfile(profileUpdateRequest);
        verify(userModelAssembler, times(1)).toModel(nebulaUser);
    }

    @Test
    void updateUserProfile_invalidToken_returnsForbidden() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken, userId)).thenReturn(false);

        // Act
        ResponseEntity<Response> response = userController.updateUserProfile(invalidToken, profileUpdateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(invalidToken, userId);
        verify(userUpdater, never()).updateProfile(any());
        verify(userModelAssembler, never()).toModel(any());
    }

    @Test
    void updateUserSettings_validTokenAndMatchingUserId_returnsUpdatedSettings() {
        // Arrange
        try (var mockedStaticUserSettingsMapper = mockStatic(UserSettingsMapper.class);
             var mockedStaticUserSettingsApiMapper = mockStatic(UserSettingsApiMapper.class)) {

            when(tokenProvider.isValid(validToken, userId)).thenReturn(true);
            when(UserSettingsMapper.requestToSettings(userSettingsRequest)).thenReturn(userSettings);
            when(userUpdater.updateSettings(userSettings)).thenReturn(userSettings);
            when(UserSettingsApiMapper.toResponse(userSettings)).thenReturn(userSettingsResponse);

            // Act
            ResponseEntity<Response> response = userController.updateUserSettings(validToken, userSettingsRequest);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(userSettingsResponse, response.getBody());
            verify(tokenProvider, times(1)).isValid(validToken, userId);
            verify(userUpdater, times(1)).updateSettings(userSettings);
            mockedStaticUserSettingsMapper.verify(() -> UserSettingsMapper.requestToSettings(userSettingsRequest));
            mockedStaticUserSettingsApiMapper.verify(() -> UserSettingsApiMapper.toResponse(userSettings));
        }
    }

    @Test
    void updateUserSettings_invalidToken_returnsForbidden() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken, userId)).thenReturn(false);

        // Act
        ResponseEntity<Response> response = userController.updateUserSettings(invalidToken, userSettingsRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(invalidToken, userId);
        verify(userUpdater, never()).updateSettings(any());
    }

}
