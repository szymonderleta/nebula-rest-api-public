package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import pl.derleta.nebula.controller.response.AccessResponse;
import pl.derleta.nebula.controller.response.JwtTokenResponse;
import pl.derleta.nebula.controller.response.Response;
import pl.derleta.nebula.domain.types.AccessResponseType;
import pl.derleta.nebula.util.HttpAuthClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class TokenUpdaterImplTest {

    @Mock
    private HttpAuthClient httpAuthServClient;

    @InjectMocks
    private TokenUpdaterImpl tokenUpdater;

    @Test
    void refreshAccess_shouldReturnSuccessResponse_whenRequestIsValid() {
        // Arrange
        String refreshToken = "valid-refresh-token";
        AccessResponse expectedResponse = new AccessResponse(Map.of(), true, "ACCESS_REFRESHED");

        when(httpAuthServClient.refreshAccess(refreshToken)).thenReturn(expectedResponse);

        // Act
        Response response = tokenUpdater.refreshAccess(refreshToken);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void refreshAccess_shouldReturnFailureResponse_whenRequestFails() {
        // Arrange
        String refreshToken = "valid-refresh-token";
        AccessResponse expectedResponse = null;

        when(httpAuthServClient.refreshAccess(refreshToken)).thenReturn(expectedResponse);

        // Act
        Response response = tokenUpdater.refreshAccess(refreshToken);

        // Assert
        assertEquals(expectedResponse, response);
    }

}