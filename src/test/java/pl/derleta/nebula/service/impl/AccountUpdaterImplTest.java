package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.derleta.nebula.controller.request.*;
import pl.derleta.nebula.controller.response.AccountResponse;
import pl.derleta.nebula.controller.response.JwtTokenResponse;
import pl.derleta.nebula.domain.entity.GenderEntity;
import pl.derleta.nebula.domain.entity.NationalityEntity;
import pl.derleta.nebula.domain.entity.UserEntity;
import pl.derleta.nebula.domain.rest.Role;
import pl.derleta.nebula.domain.rest.UserAccount;
import pl.derleta.nebula.domain.rest.UserRoles;
import pl.derleta.nebula.domain.types.AccountResponseType;
import pl.derleta.nebula.repository.*;
import pl.derleta.nebula.util.HttpAuthClient;

import java.sql.Date;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountUpdaterImplTest {

    @Mock
    private HttpAuthClient httpAuthServClient;

    @Mock
    private UserSettingsRepository settingsRepository;

    @Mock
    private UsersGamesRepository gamesRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NationalityRepository nationalityRepository;

    @Mock
    private GenderRepository genderRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private AchievementRepository achievementRepository;

    @InjectMocks
    private AccountUpdaterImpl accountUpdater;

    @Test
    void register_shouldReturnSuccessResponse_whenRegistrationIsSuccessful() {
        // Arrange
        NationalityEntity mockNationality = new NationalityEntity(1, "United States", "USA", null);
        when(nationalityRepository.findById(anyInt())).thenReturn(Optional.of(mockNationality));
        GenderEntity mockGender = new GenderEntity(1, "Male");
        when(genderRepository.findById(anyInt())).thenReturn(Optional.of(mockGender));

        AccountRegistrationRequest registrationRequest = AccountRegistrationRequest.builder()
                .login("username")
                .email("email@example.com")
                .password("password")
                .birthdate(Date.valueOf("1990-01-01"))
                .nationality(1)
                .gender(1)
                .build();

        AccountResponse authServResponse = new AccountResponse(true, AccountResponseType.VERIFICATION_MAIL_FROM_REGISTRATION);
        when(accountUpdater.httpAuthServClient.registerUser(any(AuthServRegistrationRequest.class)))
                .thenReturn(authServResponse);

        UserAccount mockUserAccount = new UserAccount(1L, "testUSer", "test@mail.com");
        UserRoles mockUserRoles = new UserRoles(mockUserAccount, Set.of(new Role(1, "USER_ROLE")));

        when(accountUpdater.httpAuthServClient.getAccount(registrationRequest)).thenReturn(mockUserRoles);

        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setId(1L);
        mockUserEntity.setLogin("username");
        mockUserEntity.setEmail("email@example.com");
        mockUserEntity.setBirthDate(Date.valueOf("1990-01-01"));
        mockUserEntity.setAge(33);
        mockUserEntity.setUpdatedAt(Instant.now());

        when(userRepository.save(any())).thenReturn(mockUserEntity);

        // Act
        AccountResponse response = accountUpdater.register(registrationRequest);

        // Assert
        assertEquals(authServResponse, response);
    }

    @Test
    void register_shouldReturnFailureResponse_whenRegistrationRequestIsInvalid() {
        // Arrange
        Request invalidRequest = new Request() {
        }; // Using an invalid type
        AccountResponse expectedResponse = new AccountResponse(false, AccountResponseType.NEBULA_BAD_REGISTRATION_REQUEST_INSTANCE);

        // Act
        AccountResponse response = accountUpdater.register(invalidRequest);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void register_shouldReturnFailureResponse_whenNebulaRegistrationFails() {
        // Arrange
        AccountRegistrationRequest registrationRequest = AccountRegistrationRequest.builder()
                .login("username")
                .email("email@example.com")
                .password("password")
                .birthdate(null)
                .nationality(1)
                .gender(1)
                .build();

        AccountResponse expectedResponse = new AccountResponse(false, AccountResponseType.NEBULA_BAD_REGISTRATION_RESPONSE_INSTANCE);

        when(accountUpdater.httpAuthServClient.registerUser(any(AuthServRegistrationRequest.class)))
                .thenReturn(new AccountResponse(false, AccountResponseType.NEBULA_BAD_REGISTRATION_RESPONSE_INSTANCE));
        when(accountUpdater.httpAuthServClient.getAccount(registrationRequest)).thenReturn(new UserRoles());
        when(userRepository.save(any())).thenReturn(null);

        // Act
        AccountResponse response = accountUpdater.register(registrationRequest);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void register_shouldReturnFailureResponse_whenAuthServResponseFails() {
        // Arrange
        AccountRegistrationRequest registrationRequest = AccountRegistrationRequest.builder()
                .login("username")
                .email("email@example.com")
                .password("password")
                .birthdate(null)
                .nationality(1)
                .gender(1)
                .build();

        AuthServRegistrationRequest authServRequest = AuthServRegistrationRequest.builder()
                .username("username")
                .email("email@example.com")
                .encryptedPassword("password")
                .build();

        AccountResponse authServResponse = new AccountResponse(false, AccountResponseType.NEBULA_BAD_REGISTRATION_RESPONSE_INSTANCE);

        when(httpAuthServClient.registerUser(authServRequest)).thenReturn(authServResponse);
        when(httpAuthServClient.registerUser(any(AuthServRegistrationRequest.class)))
                .thenReturn(new AccountResponse(false, AccountResponseType.NEBULA_BAD_REGISTRATION_RESPONSE_INSTANCE));

        // Act
        AccountResponse response = accountUpdater.register(registrationRequest);

        // Assert
        assertEquals(authServResponse, response);
    }

    @Test
    void confirm_shouldReturnSuccessResponse_whenConfirmationIsSuccessful() {
        // Arrange
        UserConfirmationRequest confirmationRequest = new UserConfirmationRequest(1L, "validToken");
        AccountResponse expectedResponse = new AccountResponse(true, null);

        when(httpAuthServClient.confirmAccount(confirmationRequest)).thenReturn(expectedResponse);

        // Act
        AccountResponse response = accountUpdater.confirm(confirmationRequest);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void confirm_shouldReturnFailureResponse_whenConfirmationFails() {
        // Arrange
        UserConfirmationRequest confirmationRequest = new UserConfirmationRequest(1L, "invalidToken");
        AccountResponse expectedResponse = new AccountResponse(false, null);

        when(httpAuthServClient.confirmAccount(confirmationRequest)).thenReturn(expectedResponse);

        // Act
        AccountResponse response = accountUpdater.confirm(confirmationRequest);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void unlock_shouldReturnSuccessResponse_whenUnlockIsSuccessful() {
        // Arrange
        Long userId = 123L;
        AccountResponse expectedResponse = new AccountResponse(true, null);

        when(httpAuthServClient.unlockAccount(userId)).thenReturn(expectedResponse);

        // Act
        AccountResponse response = accountUpdater.unlock(userId);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void unlock_shouldReturnFailureResponse_whenUnlockFails() {
        // Arrange
        Long userId = 123L;
        AccountResponse expectedResponse = new AccountResponse(false, null);

        when(httpAuthServClient.unlockAccount(userId)).thenReturn(expectedResponse);

        // Act
        AccountResponse response = accountUpdater.unlock(userId);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void resetPassword_shouldReturnSuccessResponse_whenResetIsSuccessful() {
        // Arrange
        String email = "user@example.com";
        AccountResponse expectedResponse = new AccountResponse(true, null);

        when(httpAuthServClient.resetPassword(email)).thenReturn(expectedResponse);

        // Act
        AccountResponse response = accountUpdater.resetPassword(email);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void resetPassword_shouldReturnFailureResponse_whenResetFails() {
        // Arrange
        String email = "user@example.com";
        AccountResponse expectedResponse = new AccountResponse(false, null);

        when(httpAuthServClient.resetPassword(email)).thenReturn(expectedResponse);

        // Act
        AccountResponse response = accountUpdater.resetPassword(email);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void generateToken_shouldReturnSuccessResponse_whenRequestIsValid() {
        // Arrange
        AuthEmailRequest request = new AuthEmailRequest("user@example.com", "password123");
        JwtTokenResponse expectedResponse = new JwtTokenResponse(Map.of("cookie-header", "cookie-header"), "username", "user@example.com");

        when(httpAuthServClient.generateToken(request)).thenReturn(expectedResponse);

        // Act
        JwtTokenResponse response = accountUpdater.generateToken(request);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void generateToken_shouldReturnFailureResponse_whenRequestFails() {
        // Arrange
        AuthEmailRequest request = new AuthEmailRequest("user@example.com", "wrongPassword");
        JwtTokenResponse expectedResponse = null;

        when(httpAuthServClient.generateToken(request)).thenReturn(expectedResponse);

        // Act
        JwtTokenResponse response = accountUpdater.generateToken(request);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void updatePassword_shouldReturnSuccessResponse_whenUpdateIsSuccessful() {
        // Arrange
        String jwtToken = "validJwtToken";
        PasswordUpdateRequest request = new PasswordUpdateRequest(
                123L,
                "user@example.com",
                "currentPass123",
                "newPass456"
        );
        AccountResponse expectedResponse = new AccountResponse(true, null);

        when(httpAuthServClient.updatePassword(jwtToken, request)).thenReturn(expectedResponse);

        // Act
        AccountResponse response = accountUpdater.updatePassword(jwtToken, request);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void updatePassword_shouldReturnFailureResponse_whenUpdateFails() {
        // Arrange
        String jwtToken = "validJwtToken";
        PasswordUpdateRequest request = new PasswordUpdateRequest(
                123L,
                "user@example.com",
                "currentPass123",
                "newPass456"
        );
        AccountResponse expectedResponse = new AccountResponse(false, null);

        when(httpAuthServClient.updatePassword(jwtToken, request)).thenReturn(expectedResponse);

        // Act
        AccountResponse response = accountUpdater.updatePassword(jwtToken, request);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void updatePassword_shouldHandleNullToken() {
        // Arrange
        String jwtToken = null;
        PasswordUpdateRequest request = new PasswordUpdateRequest(
                123L,
                "user@example.com",
                "currentPass123",
                "newPass456"
        );
        AccountResponse expectedResponse = new AccountResponse(false, null);

        when(httpAuthServClient.updatePassword(jwtToken, request)).thenReturn(expectedResponse);

        // Act
        AccountResponse response = accountUpdater.updatePassword(jwtToken, request);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void updatePassword_shouldHandleInvalidRequest() {
        // Arrange
        String jwtToken = "validJwtToken";
        PasswordUpdateRequest request = new PasswordUpdateRequest(
                123L,
                "user@example.com",
                "currentPass123",
                "newPass456"
        );
        AccountResponse expectedResponse = new AccountResponse(false, null);

        when(httpAuthServClient.updatePassword(jwtToken, request)).thenReturn(expectedResponse);

        // Act
        AccountResponse response = accountUpdater.updatePassword(jwtToken, request);

        // Assert
        assertEquals(expectedResponse, response);
    }

}
