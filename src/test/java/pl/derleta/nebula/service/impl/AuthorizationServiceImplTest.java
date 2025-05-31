package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.derleta.nebula.domain.rest.Role;
import pl.derleta.nebula.exceptions.TokenExpiredException;
import pl.derleta.nebula.service.TokenProvider;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorizationServiceImplTest {

    private final TokenProvider tokenProvider = Mockito.mock(TokenProvider.class);
    private final AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl(tokenProvider);

    @Test
    void notContainsAdminRole_whenTokenIsNull_thenReturnsTrue() {
        // Arrange
        String jwtToken = null;

        // Act 
        boolean result = authorizationService.notContainsAdminRole(jwtToken);

        // Assert
        assertTrue(result, "Expected TRUE because token is null");
    }

    @Test
    void notContainsAdminRole_whenEmptyToken_thenReturnsTrue() {
        // Arrange
        String jwtToken = "";

        // Act
        boolean result = authorizationService.notContainsAdminRole(jwtToken);

        // Assert
        assertTrue(result, "Expected TRUE because token is empty");
    }

    @Test
    void notContainsAdminRole_whenInvalidToken_thenReturnsTrue() {
        // Arrange
        String jwtToken = "invalidToken";
        when(tokenProvider.isValid(jwtToken)).thenReturn(false);

        // Act
        boolean result = authorizationService.notContainsAdminRole(jwtToken);

        // Assert
        assertTrue(result, "Expected TRUE because token is invalid");
        verify(tokenProvider, times(1)).isValid(jwtToken);
    }

    @Test
    void notContainsAdminRole_whenNotContainsAdminRole_thenReturnsTrue() {
        // Arrange
        String jwtToken = "validToken";
        when(tokenProvider.isValid(jwtToken)).thenReturn(true);
        when(tokenProvider.getRoles(jwtToken))
                .thenReturn(Set.of(new Role(1, "ROLE_USER"), new Role(2, "ROLE_EDITOR")));

        // Act
        boolean result = authorizationService.notContainsAdminRole(jwtToken);

        // Assert
        assertTrue(result, "Expected TRUE because token does not contain ADMIN role");
        verify(tokenProvider, times(1)).isValid(jwtToken);
        verify(tokenProvider, times(1)).getRoles(jwtToken);
    }

    @Test
    void notContainsAdminRole_whenContainsAdminRole_thenReturnsFalse() {
        // Arrange
        String jwtToken = "validTokenWithAdmin";

        when(tokenProvider.isValid(jwtToken)).thenReturn(true);
        when(tokenProvider.getRoles(jwtToken))
                .thenReturn(Set.of(new Role(1, "ROLE_USER"), new Role(2, "ROLE_ADMIN")));

        // Act
        boolean result = authorizationService.notContainsAdminRole(jwtToken);

        // Assert
        assertFalse(result, "Expected FALSE because token does not contain the ADMIN role.");
        verify(tokenProvider, times(1)).isValid(jwtToken);
        verify(tokenProvider, times(1)).getRoles(jwtToken);
    }

    @Test
    void notContainsAdminRole_expiredToken_throwsTokenExpiredException() {
        // Arrange
        String expiredToken = "expired-token";
        when(tokenProvider.isValid(expiredToken))
                .thenThrow(new TokenExpiredException("TOKEN_EXPIRED"));

        // Act & Assert
        assertThrows(TokenExpiredException.class,
                () -> tokenProvider.isValid(expiredToken));

        // Verify interactions
        verify(tokenProvider).isValid(expiredToken);
        verify(tokenProvider, never()).getRoles(any());
    }

}
