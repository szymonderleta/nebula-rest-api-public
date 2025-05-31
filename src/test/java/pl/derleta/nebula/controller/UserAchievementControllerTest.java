package pl.derleta.nebula.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.derleta.nebula.controller.assembler.UserAchievementModelAssembler;
import pl.derleta.nebula.controller.request.UserAchievementFilterRequest;
import pl.derleta.nebula.controller.mapper.UserAchievementApiMapper;
import pl.derleta.nebula.controller.response.UserAchievementResponse;
import pl.derleta.nebula.domain.model.Achievement;
import pl.derleta.nebula.domain.model.AchievementLevel;
import pl.derleta.nebula.domain.model.UserAchievement;
import pl.derleta.nebula.exceptions.TokenExpiredException;
import pl.derleta.nebula.service.TokenProvider;
import pl.derleta.nebula.service.UserAchievementProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAchievementControllerTest {

    @Mock
    private UserAchievementProvider userAchievementProvider;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private UserAchievementModelAssembler userAchievementModelAssembler;

    @InjectMocks
    private UserAchievementController userAchievementController;

    private final String validToken = "valid-token";
    private final Long userId = 1000L;
    private final Integer achievementId = 1;
    private UserAchievement userAchievement;
    private UserAchievementResponse userAchievementResponse;
    private List<UserAchievement> userAchievements;
    private List<UserAchievementResponse> userAchievementResponses;
    private Page<UserAchievement> userAchievementPage;
    private Page<UserAchievementResponse> userAchievementResponsePage;

    @BeforeEach
    void setUp() {
        List<AchievementLevel> levels = Arrays.asList(
                new AchievementLevel(1, 25),
                new AchievementLevel(2, 50)
        );

        Achievement achievement = new Achievement(
                achievementId, 
                "Achievement 1", 
                0, 
                100, 
                "Description 1", 
                "https://example.com/icon1.png", 
                levels
        );

        userAchievement = new UserAchievement(
                userId, 
                achievementId, 
                30, 
                1, 
                "30%", 
                achievement
        );

        userAchievementResponse = UserAchievementResponse.builder()
                .userId(userId)
                .achievementId(achievementId)
                .value(30)
                .level(1)
                .progress("30%")
                .achievement(achievement)
                .build();

        Achievement achievement2 = new Achievement(
                2, 
                "Achievement 2", 
                0, 
                200, 
                "Description 2", 
                "https://example.com/icon2.png", 
                levels
        );

        UserAchievement userAchievement2 = new UserAchievement(
                userId, 
                2, 
                60, 
                2, 
                "60%", 
                achievement2
        );

        userAchievements = Arrays.asList(userAchievement, userAchievement2);

        UserAchievementResponse userAchievementResponse2 = UserAchievementResponse.builder()
                .userId(userId)
                .achievementId(2)
                .value(60)
                .level(2)
                .progress("60%")
                .achievement(achievement2)
                .build();

        userAchievementResponses = Arrays.asList(userAchievementResponse, userAchievementResponse2);

        userAchievementPage = new PageImpl<>(userAchievements, PageRequest.of(0, 10), userAchievements.size());
        userAchievementResponsePage = new PageImpl<>(userAchievementResponses, PageRequest.of(0, 10), userAchievementResponses.size());
    }

    @Test
    void get_validTokenAndMatchingUserId_returnsUserAchievementResponse() {
        // Arrange
        when(tokenProvider.isValid(validToken)).thenReturn(true);
        when(tokenProvider.getUserId(validToken)).thenReturn(userId);
        when(userAchievementProvider.get(userId, achievementId)).thenReturn(userAchievement);
        when(userAchievementModelAssembler.toModel(userAchievement)).thenReturn(userAchievementResponse);

        // Act
        ResponseEntity<UserAchievementResponse> response = userAchievementController.get(userId, achievementId, validToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userAchievementResponse, response.getBody());
        verify(tokenProvider, times(1)).isValid(validToken);
        verify(tokenProvider, times(1)).getUserId(validToken);
        verify(userAchievementProvider, times(1)).get(userId, achievementId);
        verify(userAchievementModelAssembler, times(1)).toModel(userAchievement);
    }

    @Test
    void get_validTokenButDifferentUserId_returnsUnauthorized() {
        // Arrange
        Long differentUserId = 2000L;
        when(tokenProvider.isValid(validToken)).thenReturn(true);
        when(tokenProvider.getUserId(validToken)).thenReturn(userId);

        // Act
        ResponseEntity<UserAchievementResponse> response = userAchievementController.get(differentUserId, achievementId, validToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(validToken);
        verify(tokenProvider, times(1)).getUserId(validToken);
        verify(userAchievementProvider, never()).get(any(), any());
        verify(userAchievementModelAssembler, never()).toModel(any());
    }

    @Test
    void get_invalidToken_returnsForbidden() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<UserAchievementResponse> response = userAchievementController.get(userId, achievementId, invalidToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(invalidToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(userAchievementProvider, never()).get(any(), any());
        verify(userAchievementModelAssembler, never()).toModel(any());
    }

    @Test
    void get_expiredToken_throwsTokenExpiredException() {
        // Arrange
        String expiredToken = "expired-token";
        when(tokenProvider.isValid(expiredToken))
                .thenThrow(new TokenExpiredException("TOKEN_EXPIRED"));

        // Act & Assert
        assertThrows(TokenExpiredException.class,
                () -> userAchievementController.get(userId, achievementId, expiredToken));

        // Verify interactions
        verify(tokenProvider).isValid(expiredToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(userAchievementProvider, never()).get(any(), any());
        verify(userAchievementModelAssembler, never()).toModel(any());
    }

    @Test
    void getList_validToken_returnsUserAchievementResponses() {
        // Arrange
        when(tokenProvider.isValid(validToken)).thenReturn(true);
        when(tokenProvider.getUserId(validToken)).thenReturn(userId);
        when(userAchievementProvider.getList(userId)).thenReturn(userAchievements);
        when(userAchievementModelAssembler.toModel(userAchievements.get(0))).thenReturn(userAchievementResponses.get(0));
        when(userAchievementModelAssembler.toModel(userAchievements.get(1))).thenReturn(userAchievementResponses.get(1));

        // Act
        ResponseEntity<List<UserAchievementResponse>> response = userAchievementController.getList(validToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(userAchievementResponses, response.getBody());
        verify(tokenProvider, times(1)).isValid(validToken);
        verify(tokenProvider, times(1)).getUserId(validToken);
        verify(userAchievementProvider, times(1)).getList(userId);
        verify(userAchievementModelAssembler, times(1)).toModel(userAchievements.get(0));
        verify(userAchievementModelAssembler, times(1)).toModel(userAchievements.get(1));
    }

    @Test
    void getList_invalidToken_returnsForbidden() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<List<UserAchievementResponse>> response = userAchievementController.getList(invalidToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(invalidToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(userAchievementProvider, never()).getList(any());
        verify(userAchievementModelAssembler, never()).toModel(any());
    }

    @Test
    void getList_expiredToken_throwsTokenExpiredException() {
        // Arrange
        String expiredToken = "expired-token";
        when(tokenProvider.isValid(expiredToken))
                .thenThrow(new TokenExpiredException("TOKEN_EXPIRED"));

        // Act & Assert
        assertThrows(TokenExpiredException.class,
                () -> userAchievementController.getList(expiredToken));

        // Verify interactions
        verify(tokenProvider).isValid(expiredToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(userAchievementProvider, never()).get(any(), any());
        verify(userAchievementModelAssembler, never()).toModel(any());
    }

    @Test
    void getPage_validToken_returnsPageOfUserAchievementResponses() {
        // Arrange
        when(tokenProvider.isValid(validToken)).thenReturn(true);
        when(tokenProvider.getUserId(validToken)).thenReturn(userId);
        when(userAchievementProvider.getPage(any(UserAchievementFilterRequest.class))).thenReturn(userAchievementPage);
        try (var mockedStatic = mockStatic(UserAchievementApiMapper.class)) {
            mockedStatic.when(() -> UserAchievementApiMapper.toPageResponse(userAchievementPage))
                    .thenReturn(userAchievementResponsePage);

            // Act
            ResponseEntity<Page<UserAchievementResponse>> response = userAchievementController.getPage(
                    0, 10, "achievementId", "asc", 5, "less or equal", validToken);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(userAchievementResponsePage, response.getBody());
            verify(tokenProvider, times(1)).isValid(validToken);
            verify(tokenProvider, times(1)).getUserId(validToken);
            verify(userAchievementProvider, times(1)).getPage(any(UserAchievementFilterRequest.class));
            mockedStatic.verify(() -> UserAchievementApiMapper.toPageResponse(userAchievementPage));
        }
    }

    @Test
    void getPage_invalidToken_returnsForbidden() {
        // Arrange
        String invalidToken = "invalid-token";
        when(tokenProvider.isValid(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<Page<UserAchievementResponse>> response = userAchievementController.getPage(
                0, 10, "achievementId", "asc", 5, "less or equal", invalidToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(tokenProvider, times(1)).isValid(invalidToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(userAchievementProvider, never()).getPage(any());
    }

    @Test
    void getPage_expiredToken_throwsTokenExpiredException() {
        // Arrange
        String expiredToken = "expired-token";
        when(tokenProvider.isValid(expiredToken))
                .thenThrow(new TokenExpiredException("TOKEN_EXPIRED"));

        // Act & Assert
        assertThrows(TokenExpiredException.class,
                () -> userAchievementController.getPage(
                        0, 10, "achievementId", "asc", 5, "less or equal", expiredToken));

        // Verify interactions
        verify(tokenProvider).isValid(expiredToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(userAchievementProvider, never()).get(any(), any());
        verify(userAchievementModelAssembler, never()).toModel(any());
    }

}
