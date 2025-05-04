package pl.derleta.nebula.controller.mapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.derleta.nebula.controller.request.AccountRegistrationRequest;
import pl.derleta.nebula.controller.request.AuthServRegistrationRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class AuthServRegistrationRequestMapperTest {

    @Test
    void getAccountAuthRegistration_validAccountRequest_returnsMappedAuthRequest() {
        // Arrange
        String login = "testUser";
        String email = "test@example.com";
        String password = "password123";
        String encryptedPasswordMock = "$2a$10$mockedEncryptedPassword";

        AccountRegistrationRequest accountRegistrationRequest = AccountRegistrationRequest.builder()
                .login(login)
                .email(email)
                .password(password)
                .build();

        BCryptPasswordEncoder passwordEncoderMock = Mockito.mock(BCryptPasswordEncoder.class);
        when(passwordEncoderMock.encode(password)).thenReturn(encryptedPasswordMock);

        // Act
        AuthServRegistrationRequest authServRegistrationRequest = AuthServRegistrationRequestMapper.getAccountAuthRegistration(accountRegistrationRequest);

        // Assert
        assertNotNull(authServRegistrationRequest);
        assertEquals(login, authServRegistrationRequest.getUsername());
        assertEquals(email, authServRegistrationRequest.getEmail());
        assertNotNull(authServRegistrationRequest.getEncryptedPassword());
    }

}