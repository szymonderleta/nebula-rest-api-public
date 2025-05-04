package pl.derleta.nebula.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.derleta.nebula.controller.mapper.GameApiMapper;
import pl.derleta.nebula.controller.request.GameFilterRequest;
import pl.derleta.nebula.controller.request.GameNewRequest;
import pl.derleta.nebula.controller.response.GameResponse;
import pl.derleta.nebula.domain.model.Game;
import pl.derleta.nebula.service.AuthorizationService;
import pl.derleta.nebula.service.GameProvider;
import pl.derleta.nebula.service.GameUpdater;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameProvider gameProvider;

    @Mock
    private GameUpdater gameUpdater;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private GameController gameController;

    private Game game1;
    private Game game2;
    private GameResponse gameResponse1;
    private GameResponse gameResponse2;
    private List<Game> gameList;
    private List<GameResponse> gameResponseList;
    private Page<Game> gamePage;
    private Page<GameResponse> gameResponsePage;
    private GameNewRequest gameNewRequest;
    private String accessToken;

    @BeforeEach
    void setUp() {
        // Set up test data
        game1 = new Game(1, "Game 1", true, "icon1.png", "page1.html");
        game2 = new Game(2, "Game 2", true, "icon2.png", "page2.html");
        gameList = Arrays.asList(game1, game2);
        gamePage = new PageImpl<>(gameList);

        gameResponse1 = GameResponse.builder()
                .id(game1.id())
                .name(game1.name())
                .enable(game1.enable())
                .iconUrl(game1.iconUrl())
                .pageUrl(game1.pageUrl())
                .build();

        gameResponse2 = GameResponse.builder()
                .id(game2.id())
                .name(game2.name())
                .enable(game2.enable())
                .iconUrl(game2.iconUrl())
                .pageUrl(game2.pageUrl())
                .build();

        gameResponseList = Arrays.asList(gameResponse1, gameResponse2);
        gameResponsePage = new PageImpl<>(gameResponseList);

        gameNewRequest = new GameNewRequest("New Game", true, "newicon.png", "newpage.html");
        accessToken = "valid.access.token";
    }

    @Test
    void get_validId_returnsGameResponse() {
        // Arrange
        Integer id = 1;
        try (var mockedStatic = mockStatic(GameApiMapper.class)) {
            mockedStatic.when(() -> GameApiMapper.toResponse(game1)).thenReturn(gameResponse1);
            when(gameProvider.get(id)).thenReturn(game1);

            // Act
            ResponseEntity<GameResponse> response = gameController.get(id);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(gameResponse1, response.getBody());
            verify(gameProvider, times(1)).get(id);
            mockedStatic.verify(() -> GameApiMapper.toResponse(game1), times(1));
        }
    }

    @Test
    void getPage_returnsPageOfGameResponses() {
        // Arrange
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String sortOrder = "asc";
        String name = "";
        Boolean enable = true;

        try (var mockedStatic = mockStatic(GameApiMapper.class)) {
            when(gameProvider.get(any(GameFilterRequest.class))).thenReturn(gamePage);
            mockedStatic.when(() -> GameApiMapper.toPageResponse(gamePage)).thenReturn(gameResponsePage);

            // Act
            ResponseEntity<Page<GameResponse>> response = gameController.getPage(page, size, sortBy, sortOrder, name, enable);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(gameResponsePage, response.getBody());
            verify(gameProvider, times(1)).get(any(GameFilterRequest.class));
            mockedStatic.verify(() -> GameApiMapper.toPageResponse(gamePage), times(1));
        }
    }

    @Test
    void getEnabled_returnsListOfEnabledGameResponses() {
        // Arrange
        try (var mockedStatic = mockStatic(GameApiMapper.class)) {
            when(gameProvider.getEnabled()).thenReturn(gameList);
            mockedStatic.when(() -> GameApiMapper.toResponseList(gameList)).thenReturn(gameResponseList);

            // Act
            ResponseEntity<List<GameResponse>> response = gameController.getEnabled();

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(gameResponseList, response.getBody());
            verify(gameProvider, times(1)).getEnabled();
            mockedStatic.verify(() -> GameApiMapper.toResponseList(gameList), times(1));
        }
    }

    @Test
    void add_withAdminRole_createsGame() {
        // Arrange
        Integer nextId = 3;
        Game newGame = new Game(nextId, gameNewRequest.name(), gameNewRequest.enable(),
                gameNewRequest.iconUrl(), gameNewRequest.pageUrl());
        GameResponse newGameResponse = GameResponse.builder()
                .id(nextId)
                .name(gameNewRequest.name())
                .enable(gameNewRequest.enable())
                .iconUrl(gameNewRequest.iconUrl())
                .pageUrl(gameNewRequest.pageUrl())
                .build();

        try (var mockedStatic = mockStatic(GameApiMapper.class)) {
            when(authorizationService.notContainsAdminRole(accessToken)).thenReturn(false);
            when(gameProvider.getNextId()).thenReturn(nextId);
            when(gameUpdater.create(any(Game.class))).thenReturn(newGame);
            mockedStatic.when(() -> GameApiMapper.toResponse(newGame)).thenReturn(newGameResponse);

            // Act
            ResponseEntity<GameResponse> response = gameController.add(accessToken, gameNewRequest);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(newGameResponse, response.getBody());
            verify(authorizationService, times(1)).notContainsAdminRole(accessToken);
            verify(gameProvider, times(1)).getNextId();
            verify(gameUpdater, times(1)).create(any(Game.class));
            mockedStatic.verify(() -> GameApiMapper.toResponse(newGame), times(1));
        }
    }

    @Test
    void add_withoutAdminRole_returnsForbidden() {
        // Arrange
        when(authorizationService.notContainsAdminRole(accessToken)).thenReturn(true);

        // Act
        ResponseEntity<GameResponse> response = gameController.add(accessToken, gameNewRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(authorizationService, times(1)).notContainsAdminRole(accessToken);
        verify(gameProvider, never()).getNextId();
        verify(gameUpdater, never()).create(any(Game.class));
    }

    @Test
    void update_withAdminRole_updatesGame() {
        // Arrange
        int id = 1;
        Game updatedGame = new Game(id, gameNewRequest.name(), gameNewRequest.enable(),
                gameNewRequest.iconUrl(), gameNewRequest.pageUrl());
        GameResponse updatedGameResponse = GameResponse.builder()
                .id(id)
                .name(gameNewRequest.name())
                .enable(gameNewRequest.enable())
                .iconUrl(gameNewRequest.iconUrl())
                .pageUrl(gameNewRequest.pageUrl())
                .build();

        try (var mockedStatic = mockStatic(GameApiMapper.class)) {
            when(authorizationService.notContainsAdminRole(accessToken)).thenReturn(false);
            when(gameUpdater.update(any(Game.class))).thenReturn(updatedGame);
            mockedStatic.when(() -> GameApiMapper.toResponse(updatedGame)).thenReturn(updatedGameResponse);

            // Act
            ResponseEntity<GameResponse> response = gameController.update(accessToken, id, gameNewRequest);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(updatedGameResponse, response.getBody());
            verify(authorizationService, times(1)).notContainsAdminRole(accessToken);
            verify(gameUpdater, times(1)).update(any(Game.class));
            mockedStatic.verify(() -> GameApiMapper.toResponse(updatedGame), times(1));
        }
    }

    @Test
    void update_withoutAdminRole_returnsForbidden() {
        // Arrange
        int id = 1;
        when(authorizationService.notContainsAdminRole(accessToken)).thenReturn(true);

        // Act
        ResponseEntity<GameResponse> response = gameController.update(accessToken, id, gameNewRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(authorizationService, times(1)).notContainsAdminRole(accessToken);
        verify(gameUpdater, never()).update(any(Game.class));
    }

    @Test
    void delete_withAdminRole_deletesGame() {
        // Arrange
        int id = 1;
        ResponseEntity<String> expectedResponse = ResponseEntity.ok("Game deleted");

        when(authorizationService.notContainsAdminRole(accessToken)).thenReturn(false);
        when(gameUpdater.delete(id)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> response = gameController.delete(accessToken, id);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse, response);
        verify(authorizationService, times(1)).notContainsAdminRole(accessToken);
        verify(gameUpdater, times(1)).delete(id);
    }

    @Test
    void delete_withoutAdminRole_returnsForbidden() {
        // Arrange
        int id = 1;
        when(authorizationService.notContainsAdminRole(accessToken)).thenReturn(true);

        // Act
        ResponseEntity<String> response = gameController.delete(accessToken, id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
        verify(authorizationService, times(1)).notContainsAdminRole(accessToken);
        verify(gameUpdater, never()).delete(id);
    }

}
