package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.config.JwtTokenUtil;
import pl.derleta.nebula.domain.rest.Role;
import pl.derleta.nebula.domain.token.TokenData;
import pl.derleta.nebula.exceptions.TokenExpiredException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class TokenProviderImplTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private TokenProviderImpl tokenProvider;

    private final String validToken = "valid.jwt.token";
    private final String expiredToken = "expired.jwt.token";
    private final Long userId = 123L;
    private final String email = "user@example.com";
    private Set<Role> roles;
    private TokenData tokenData;

    @BeforeEach
    void setUp() {
        roles = new HashSet<>();
        roles.add(new Role(1, "ROLE_USER"));
        roles.add(new Role(2, "ROLE_ADMIN"));

        tokenData = new TokenData(true, userId, email, validToken, roles);
    }

    @Test
    void getTokenData_shouldReturnTokenData_whenValidTokenProvided() {
        // Arrange
        when(jwtTokenUtil.getTokenData(validToken)).thenReturn(tokenData);

        // Act
        TokenData result = tokenProvider.getTokenData(validToken);

        // Assert
        assertNotNull(result);
        assertEquals(tokenData, result);
        verify(jwtTokenUtil, times(1)).getTokenData(validToken);
    }

    @Test
    void isValid_shouldReturnTrue_whenTokenIsNotExpired() {
        // Arrange
        when(jwtTokenUtil.isTokenExpired(validToken)).thenReturn(false);

        // Act
        boolean result = tokenProvider.isValid(validToken);

        // Assert
        assertTrue(result);
        verify(jwtTokenUtil, times(1)).isTokenExpired(validToken);
    }

    @Test
    void isValid_shouldReturnFalse_whenTokenIsNull() {
        // Arrange

        // Act
        boolean result = tokenProvider.isValid(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void isValid_shouldThrowException_whenTokenIsExpired() {
        // Arrange
        when(jwtTokenUtil.isTokenExpired(expiredToken)).thenReturn(true);

        // Act & Assert
        assertThrows(TokenExpiredException.class, () -> tokenProvider.isValid(expiredToken));
        verify(jwtTokenUtil, times(1)).isTokenExpired(expiredToken);
    }

    @Test
    void isValid_withUserId_shouldReturnTrue_whenTokenIsValidAndUserIdMatches() {
        // Arrange
        when(jwtTokenUtil.isTokenExpired(validToken)).thenReturn(false);
        when(jwtTokenUtil.getUserId(validToken)).thenReturn(userId);

        // Act
        boolean result = tokenProvider.isValid(validToken, userId);

        // Assert
        assertTrue(result);
        verify(jwtTokenUtil, times(1)).isTokenExpired(validToken);
        verify(jwtTokenUtil, times(1)).getUserId(validToken);
    }

    @Test
    void isValid_withUserId_shouldReturnFalse_whenTokenIsExpired() {
        // Arrange
        when(jwtTokenUtil.isTokenExpired(expiredToken)).thenReturn(true);

        // Act & Assert
        assertThrows(TokenExpiredException.class, () -> tokenProvider.isValid(expiredToken, 123L));
        verify(jwtTokenUtil, times(1)).isTokenExpired(expiredToken);
    }

    @Test
    void isValid_withUserId_shouldReturnFalse_whenTokenIsNull() {
        // Arrange

        // Act
        boolean result = tokenProvider.isValid(null, 123L);

        // Assert
        assertFalse(result);
    }

    @Test
    void isValid_withUserId_shouldReturnFalse_whenUserIdDoesNotMatch() {
        // Arrange
        when(jwtTokenUtil.isTokenExpired(validToken)).thenReturn(false);
        when(jwtTokenUtil.getUserId(validToken)).thenReturn(456L); // Different user ID

        // Act
        boolean result = tokenProvider.isValid(validToken, userId);

        // Assert
        assertFalse(result);
        verify(jwtTokenUtil, times(1)).isTokenExpired(validToken);
        verify(jwtTokenUtil, times(1)).getUserId(validToken);
    }

    @Test
    void getUserId_shouldReturnUserId_whenValidTokenProvided() {
        // Arrange
        when(jwtTokenUtil.getUserId(validToken)).thenReturn(userId);

        // Act
        Long result = tokenProvider.getUserId(validToken);

        // Assert
        assertEquals(userId, result);
        verify(jwtTokenUtil, times(1)).getUserId(validToken);
    }

    @Test
    void getRoles_shouldReturnRoles_whenValidTokenProvided() {
        // Arrange
        when(jwtTokenUtil.getRoles(validToken)).thenReturn(roles);

        // Act
        Set<Role> result = tokenProvider.getRoles(validToken);

        // Assert
        assertEquals(roles, result);
        verify(jwtTokenUtil, times(1)).getRoles(validToken);
    }

    @Test
    void getEmail_shouldReturnEmail_whenValidTokenProvided() {
        // Arrange
        when(jwtTokenUtil.getEmail(validToken)).thenReturn(email);

        // Act
        String result = tokenProvider.getEmail(validToken);

        // Assert
        assertEquals(email, result);
        verify(jwtTokenUtil, times(1)).getEmail(validToken);
    }

}
