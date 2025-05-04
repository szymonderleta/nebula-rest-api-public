package pl.derleta.nebula.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.derleta.nebula.controller.request.AccountRegistrationRequest;
import pl.derleta.nebula.controller.request.AuthEmailRequest;
import pl.derleta.nebula.controller.request.PasswordUpdateRequest;
import pl.derleta.nebula.controller.request.UserConfirmationRequest;
import pl.derleta.nebula.controller.response.AccountResponse;
import pl.derleta.nebula.controller.response.JwtTokenResponse;
import pl.derleta.nebula.controller.response.Response;
import pl.derleta.nebula.domain.types.AccountResponseType;
import pl.derleta.nebula.exceptions.HttpRequestException;
import pl.derleta.nebula.exceptions.TokenExpiredException;
import pl.derleta.nebula.service.AccountUpdater;
import pl.derleta.nebula.service.TokenProvider;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountUpdater accountUpdater;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AccountController accountController;

    private AccountRegistrationRequest registrationRequest;
    private UserConfirmationRequest confirmationRequest;
    private AuthEmailRequest authEmailRequest;
    private PasswordUpdateRequest passwordUpdateRequest;
    private AccountResponse successResponse;
    private AccountResponse failureResponse;
    private JwtTokenResponse jwtTokenResponse;

    @BeforeEach
    void setUp() {
        // Set up test data
        registrationRequest = AccountRegistrationRequest.builder()
                .login("testuser")
                .email("test@example.com")
                .password("password123")
                .birthdate(Date.valueOf("1990-01-01"))
                .nationality(1)
                .gender(1)
                .build();

        confirmationRequest = new UserConfirmationRequest(1L, "confirmation-token");

        authEmailRequest = new AuthEmailRequest("test@example.com", "password123");

        passwordUpdateRequest = new PasswordUpdateRequest(1L, "test@example.com", "oldPassword", "newPassword");

        successResponse = new AccountResponse(true, AccountResponseType.USER_CREATED_IN_NEBULA_DB);
        failureResponse = new AccountResponse(false, AccountResponseType.EMAIL_IS_NOT_UNIQUE);

        Map<String, String> cookies = Map.of("accessToken", "accessToken=123ABc", "refreshToken", "refreshToken=123ABc");
        jwtTokenResponse = new JwtTokenResponse(cookies, "testuser", "test@example.com");
    }

    @Test
    void register_success_returnsOkResponse() {
        // Arrange
        when(accountUpdater.register(registrationRequest)).thenReturn(successResponse);

        // Act
        ResponseEntity<Response> response = accountController.register(registrationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successResponse, response.getBody());
        verify(accountUpdater, times(1)).register(registrationRequest);
    }

    @Test
    void register_failure_returnsInternalServerErrorResponse() {
        // Arrange
        when(accountUpdater.register(registrationRequest)).thenReturn(failureResponse);

        // Act
        ResponseEntity<Response> response = accountController.register(registrationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(failureResponse, response.getBody());
        verify(accountUpdater, times(1)).register(registrationRequest);
    }

    @Test
    void confirm_success_returnsOkResponse() {
        // Arrange
        when(accountUpdater.confirm(confirmationRequest)).thenReturn(successResponse);

        // Act
        ResponseEntity<Response> response = accountController.confirm(confirmationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successResponse, response.getBody());
        verify(accountUpdater, times(1)).confirm(confirmationRequest);
    }

    @Test
    void confirm_expiredToken_returns400WithToken_Expired() {
        // Arrange
        when(accountUpdater.confirm(confirmationRequest)).thenThrow(new TokenExpiredException("Token expired"));

        // Act
        ResponseEntity<Response> response = accountController.confirm(confirmationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("AccountResponse(success=false, type=AccountResponseType.TOKEN_EXPIRED"));
        verify(accountUpdater, times(1)).confirm(confirmationRequest);
    }

    @Test
    void confirm_failure_returnsInternalServerErrorResponse() {
        // Arrange
        when(accountUpdater.confirm(confirmationRequest)).thenReturn(failureResponse);

        // Act
        ResponseEntity<Response> response = accountController.confirm(confirmationRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(failureResponse, response.getBody());
        verify(accountUpdater, times(1)).confirm(confirmationRequest);
    }

    @Test
    void unlock_success_returnsOkResponse() {
        // Arrange
        Long id = 1L;
        when(accountUpdater.unlock(id)).thenReturn(successResponse);

        // Act
        ResponseEntity<AccountResponse> response = accountController.unlock(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successResponse, response.getBody());
        verify(accountUpdater, times(1)).unlock(id);
    }

    @Test
    void unlock_failure_returnsUnauthorizedErrorResponse() {
        // Arrange
        Long id = 1L;
        when(accountUpdater.unlock(id)).thenReturn(failureResponse);

        // Act
        ResponseEntity<AccountResponse> response = accountController.unlock(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(failureResponse, response.getBody());
        verify(accountUpdater, times(1)).unlock(id);
    }

    @Test
    void unlock_notLocked_returns401WithBadUnlockHttpRequest() {
        // Arrange
        long id = 1L;

        when(accountUpdater.unlock(id)).thenThrow(new HttpRequestException("Account is not blocked", new IOException("")));

        // Act
        ResponseEntity<AccountResponse> response = accountController.unlock(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("AccountResponseType.BAD_UNLOCK_HTTP_REQUEST"));
        verify(accountUpdater, times(1)).unlock(id);
    }

    @Test
    void resetPassword_success_returnsOkResponse() {
        // Arrange
        String email = "test@example.com";
        when(accountUpdater.resetPassword(email)).thenReturn(successResponse);

        // Act
        ResponseEntity<AccountResponse> response = accountController.resetPassword(email);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successResponse, response.getBody());
        verify(accountUpdater, times(1)).resetPassword(email);
    }

    @Test
    void resetPassword_failure_returnsInternalServerErrorResponse() {
        // Arrange
        String email = "test@example.com";
        when(accountUpdater.resetPassword(email)).thenReturn(failureResponse);

        // Act
        ResponseEntity<AccountResponse> response = accountController.resetPassword(email);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(failureResponse, response.getBody());
        verify(accountUpdater, times(1)).resetPassword(email);
    }

    @Test
    void resetPassword_expiredToken_returns400WithToken_Expired() {
        // Arrange
        String email = "test@example.com";
        when(accountUpdater.resetPassword(email)).thenThrow(new TokenExpiredException("Token expired"));

        // Act
        ResponseEntity<AccountResponse> response = accountController.resetPassword(email);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("AccountResponse(success=false, type=AccountResponseType.PASSWORD_RESET_ACCESS_TOKEN_EXPIRED"));
        verify(accountUpdater, times(1)).resetPassword(email);
    }

    @Test
    void getToken_success_returnsOkResponse() {
        // Arrange
        when(accountUpdater.generateToken(authEmailRequest)).thenReturn(jwtTokenResponse);

        // Act
        ResponseEntity<JwtTokenResponse> response = accountController.getToken(authEmailRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtTokenResponse, response.getBody());
        assertEquals("accessToken=123ABc", response.getHeaders().getFirst("Set-Cookie"));
        verify(accountUpdater, times(1)).generateToken(authEmailRequest);
    }

    @Test
    void getToken_failure_returnsInternalServerErrorResponse() {
        // Arrange
        when(accountUpdater.generateToken(authEmailRequest)).thenReturn(null);

        // Act
        ResponseEntity<JwtTokenResponse> response = accountController.getToken(authEmailRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountUpdater, times(1)).generateToken(authEmailRequest);
    }

    @Test
    void changePassword_validToken_success_returnsOkResponse() {
        // Arrange
        String accessToken = "valid-token";
        when(tokenProvider.isValid(accessToken, passwordUpdateRequest.getUserId())).thenReturn(true);
        when(accountUpdater.updatePassword(accessToken, passwordUpdateRequest)).thenReturn(successResponse);

        // Act
        ResponseEntity<AccountResponse> response = accountController.changePassword(accessToken, passwordUpdateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successResponse, response.getBody());
        verify(tokenProvider, times(1)).isValid(accessToken, passwordUpdateRequest.getUserId());
        verify(accountUpdater, times(1)).updatePassword(accessToken, passwordUpdateRequest);
    }

    @Test
    void changePassword_validToken_failure_returnsInternalServerErrorResponse() {
        // Arrange
        String accessToken = "valid-token";
        when(tokenProvider.isValid(accessToken, passwordUpdateRequest.getUserId())).thenReturn(true);
        when(accountUpdater.updatePassword(accessToken, passwordUpdateRequest)).thenReturn(failureResponse);

        // Act
        ResponseEntity<AccountResponse> response = accountController.changePassword(accessToken, passwordUpdateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(failureResponse, response.getBody());
        verify(tokenProvider, times(1)).isValid(accessToken, passwordUpdateRequest.getUserId());
        verify(accountUpdater, times(1)).updatePassword(accessToken, passwordUpdateRequest);
    }

    @Test
    void changePassword_invalidToken_returnsForbiddenResponse() {
        // Arrange
        String accessToken = "invalid-token";
        when(tokenProvider.isValid(accessToken, passwordUpdateRequest.getUserId())).thenReturn(false);

        // Act
        ResponseEntity<AccountResponse> response = accountController.changePassword(accessToken, passwordUpdateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(accessToken, passwordUpdateRequest.getUserId());
        verify(accountUpdater, never()).updatePassword(any(), any());
    }

    @Test
    void changePassword_expiredToken_returns400WithToken_Expired() {
        // Arrange
        String accessToken = "valid-token";
        when(tokenProvider.isValid(accessToken, passwordUpdateRequest.getUserId())).thenThrow(new TokenExpiredException("Token expired"));

        // Act
        ResponseEntity<AccountResponse> response = accountController.changePassword(accessToken, passwordUpdateRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        System.out.println(response.getBody().toString());
        assertTrue(response.getBody().toString().contains("AccountResponse(success=false, type=AccountResponseType.PASSWORD_CHANGE_ACCESS_TOKEN_EXPIRED"));
        verify(tokenProvider, times(1)).isValid(accessToken, passwordUpdateRequest.getUserId());
    }

}
