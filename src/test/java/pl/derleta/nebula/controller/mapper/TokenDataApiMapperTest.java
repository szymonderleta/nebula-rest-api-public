package pl.derleta.nebula.controller.mapper;

import org.junit.jupiter.api.Test;
import pl.derleta.nebula.controller.response.TokenDataResponse;
import pl.derleta.nebula.domain.rest.Role;
import pl.derleta.nebula.domain.token.TokenData;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TokenDataApiMapperTest {

    @Test
    void toResponse_validTokenData_returnsTokenDataResponse() {
        // Arrange
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1, "USER"));
        roles.add(new Role(2, "ADMIN"));

        TokenData tokenData = new TokenData(
                true,
                1000L,
                "test@example.com",
                "valid-token-string",
                roles
        );

        // Act
        TokenDataResponse result = TokenDataApiMapper.toResponse(tokenData);

        // Assert
        assertNotNull(result);
        assertTrue(result.isValid());
        assertEquals(1000L, result.getUserId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("valid-token-string", result.getToken());
        assertEquals(roles, result.getRoles());
    }

    @Test
    void toResponse_nullTokenData_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> TokenDataApiMapper.toResponse(null)
        );
    }

}
