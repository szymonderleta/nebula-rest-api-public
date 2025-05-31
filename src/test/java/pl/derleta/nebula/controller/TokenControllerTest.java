package pl.derleta.nebula.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.derleta.nebula.controller.assembler.TokenModelAssembler;
import pl.derleta.nebula.controller.response.AccessResponse;
import pl.derleta.nebula.controller.response.Response;
import pl.derleta.nebula.controller.response.TokenDataResponse;
import pl.derleta.nebula.domain.rest.Role;
import pl.derleta.nebula.domain.token.TokenData;
import pl.derleta.nebula.domain.types.AccessResponseType;
import pl.derleta.nebula.domain.types.TokenResponseType;
import pl.derleta.nebula.exceptions.TokenExpiredException;
import pl.derleta.nebula.service.TokenProvider;
import pl.derleta.nebula.service.TokenUpdater;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenControllerTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private TokenUpdater tokenUpdater;

    @Mock
    private TokenModelAssembler tokenModelAssembler;

    @InjectMocks
    private TokenController tokenController;

    private final String validToken = "valid-token";
    private TokenData tokenData;
    private TokenDataResponse tokenDataResponse;
    private Set<Role> roles;

    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        roles.add(new Role(1, "USER"));
        roles.add(new Role(2, "ADMIN"));

        tokenData = new TokenData(true, 1000L, "test@example.com", validToken, roles);
        tokenDataResponse = TokenDataResponse.builder()
                .valid(true)
                .userId(1000L)
                .email("test@example.com")
                .token(validToken)
                .roles(roles)
                .build();
    }

    @Test
    void get_validToken_returnsTokenDataResponse() {
        // Arrange
        when(tokenProvider.isValid(validToken)).thenReturn(true);
        when(tokenProvider.getTokenData(validToken)).thenReturn(tokenData);
        when(tokenModelAssembler.toModel(tokenData)).thenReturn(tokenDataResponse);

        // Act
        ResponseEntity<TokenDataResponse> response = tokenController.get(validToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tokenDataResponse, response.getBody());
        verify(tokenProvider, times(1)).getTokenData(validToken);
        verify(tokenModelAssembler, times(1)).toModel(tokenData);
    }

    @Test
    void get_expiredToken_throwsTokenExpiredException() {
        // Arrange
        String expiredToken = "expired-token";
        when(tokenProvider.isValid(expiredToken))
                .thenThrow(new TokenExpiredException("TOKEN_EXPIRED"));

        // Act & Assert
        assertThrows(TokenExpiredException.class,
                () -> tokenController.get(expiredToken));

        // Verify interactions
        verify(tokenProvider).isValid(expiredToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(tokenModelAssembler, never()).toModel(any());
    }


    @Test
    void isValid_validToken_returnsTrue() {
        // Arrange
        when(tokenProvider.isValid(validToken)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = tokenController.isValid(validToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
        verify(tokenProvider, times(1)).isValid(validToken);
    }

    @Test
    void isValid_invalidToken_returnsFalse() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<Boolean> response = tokenController.isValid(invalidToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotEquals(Boolean.TRUE, response.getBody());
        verify(tokenProvider, times(1)).isValid(invalidToken);
    }

    @Test
    void isValid_expiredToken_throwsTokenExpiredException() {
        // Arrange
        String expiredToken = "expired-token";
        when(tokenProvider.isValid(expiredToken))
                .thenThrow(new TokenExpiredException("TOKEN_EXPIRED"));

        // Act & Assert
        assertThrows(TokenExpiredException.class,
                () -> tokenController.isValid(expiredToken));

        // Verify interactions
        verify(tokenProvider).isValid(expiredToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(tokenModelAssembler, never()).toModel(any());
    }


    @Test
    void getRoles_validToken_returnsRoles() {
        // Arrange
        when(tokenProvider.isValid(validToken)).thenReturn(true);
        when(tokenProvider.getRoles(validToken)).thenReturn(roles);

        // Act
        ResponseEntity<Set<Role>> response = tokenController.getRoles(validToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roles, response.getBody());
        verify(tokenProvider, times(1)).isValid(validToken);
        verify(tokenProvider, times(1)).getRoles(validToken);
    }

    @Test
    void getRoles_invalidToken_returnsBadRequest() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<Set<Role>> response = tokenController.getRoles(invalidToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(invalidToken);
        verify(tokenProvider, never()).getRoles(any());
    }


    @Test
    void getRoles_expiredToken_throwsTokenExpiredException() {
        // Arrange
        String expiredToken = "expired-token";
        when(tokenProvider.isValid(expiredToken))
                .thenThrow(new TokenExpiredException("TOKEN_EXPIRED"));

        // Act & Assert
        assertThrows(TokenExpiredException.class,
                () -> tokenController.getRoles(expiredToken));

        // Verify interactions
        verify(tokenProvider).isValid(expiredToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(tokenModelAssembler, never()).toModel(any());
    }

    @Test
    void getEmail_validToken_returnsEmail() {
        // Arrange
        when(tokenProvider.isValid(validToken)).thenReturn(true);
        when(tokenProvider.getEmail(validToken)).thenReturn("test@example.com");

        // Act
        ResponseEntity<String> response = tokenController.getEmail(validToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@example.com", response.getBody());
        verify(tokenProvider, times(1)).isValid(validToken);
        verify(tokenProvider, times(1)).getEmail(validToken);
    }

    @Test
    void getEmail_expiredToken_throwsTokenExpiredException() {
        // Arrange
        String expiredToken = "expired-token";
        when(tokenProvider.isValid(expiredToken))
                .thenThrow(new TokenExpiredException("TOKEN_EXPIRED"));

        // Act & Assert
        assertThrows(TokenExpiredException.class,
                () -> tokenController.getEmail(expiredToken));

        // Verify interactions
        verify(tokenProvider).isValid(expiredToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(tokenModelAssembler, never()).toModel(any());
    }


    @Test
    void getEmail_invalidToken_returnsBadRequest() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<String> response = tokenController.getEmail(invalidToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(invalidToken);
        verify(tokenProvider, never()).getEmail(any());
    }

    @Test
    void getId_validToken_returnsUserId() {
        // Arrange
        when(tokenProvider.isValid(validToken)).thenReturn(true);
        when(tokenProvider.getUserId(validToken)).thenReturn(1000L);

        // Act
        ResponseEntity<Long> response = tokenController.getId(validToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1000L, response.getBody());
        verify(tokenProvider, times(1)).isValid(validToken);
        verify(tokenProvider, times(1)).getUserId(validToken);
    }

    @Test
    void getId_invalidToken_returnsBadRequest() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<Long> response = tokenController.getId(invalidToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(invalidToken);
        verify(tokenProvider, never()).getUserId(any());
    }

    @Test
    void getId_expiredToken_throwsTokenExpiredException() {
        // Arrange
        String expiredToken = "expired-token";
        when(tokenProvider.isValid(expiredToken))
                .thenThrow(new TokenExpiredException("TOKEN_EXPIRED"));

        // Act & Assert
        assertThrows(TokenExpiredException.class,
                () -> tokenController.getId(expiredToken));

        // Verify interactions
        verify(tokenProvider).isValid(expiredToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(tokenModelAssembler, never()).toModel(any());
    }


    @Test
    void refreshAccess_validToken_returnsTrue() {
        // Arrange
        String validToken = "valid-token ";
        AccessResponse refreshTokenResponse = new AccessResponse(Map.of(), true, "ACCESS_REFRESHED");
        when(tokenProvider.isValid(validToken)).thenReturn(true);
        when(tokenUpdater.refreshAccess(validToken)).thenReturn(refreshTokenResponse);

        // Act
        ResponseEntity<Response> response = tokenController.refreshAccess(validToken);
        AccessResponse tokenResponse = (AccessResponse) response.getBody();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(tokenResponse);
        assertInstanceOf(AccessResponse.class, response.getBody());
        assertTrue(tokenResponse.isSuccess());

        verify(tokenProvider, times(1)).isValid(validToken);
        verify(tokenUpdater, times(1)).refreshAccess(validToken);
    }

    @Test
    void refreshAccess_invalidToken_returnsBadRequest() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<Response> response = tokenController.refreshAccess(invalidToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(tokenProvider, times(1)).isValid(invalidToken);
    }

    @Test
    void refreshAccess_nullToken_returnsBadRequest() {
        // Arrange
        when(tokenProvider.isValid(null)).thenReturn(false);

        // Act
        ResponseEntity<Response> response = tokenController.refreshAccess(null);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(tokenProvider, times(1)).isValid(null);
    }

    @Test
    void refreshAccess_expiredToken_throwsTokenExpiredException() {
        // Arrange
        String expiredToken = "expired-token";
        when(tokenProvider.isValid(expiredToken))
                .thenThrow(new TokenExpiredException("TOKEN_EXPIRED"));

        // Act & Assert
        assertThrows(TokenExpiredException.class,
                () -> tokenController.refreshAccess(expiredToken));

        // Verify interactions
        verify(tokenProvider).isValid(expiredToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(tokenModelAssembler, never()).toModel(any());
    }

}
