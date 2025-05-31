package pl.derleta.nebula.controller.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.derleta.nebula.controller.response.GameResponse;
import pl.derleta.nebula.domain.model.Game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameApiMapperTest {

    @Test
    void toResponseList_validGames_returnsGameResponses() {
        // Arrange
        Game game1 = new Game(1, "Game 1", true, "https://example.com/icon1.png", "https://example.com/game1");
        Game game2 = new Game(2, "Game 2", false, "https://example.com/icon2.png", "https://example.com/game2");
        List<Game> games = Arrays.asList(game1, game2);

        // Act
        List<GameResponse> result = GameApiMapper.toResponseList(games);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        GameResponse response1 = result.getFirst();
        assertEquals(1, response1.getId());
        assertEquals("Game 1", response1.getName());
        assertTrue(response1.getEnable());
        assertEquals("https://example.com/icon1.png", response1.getIconUrl());
        assertEquals("https://example.com/game1", response1.getPageUrl());
        
        GameResponse response2 = result.get(1);
        assertEquals(2, response2.getId());
        assertEquals("Game 2", response2.getName());
        assertFalse(response2.getEnable());
        assertEquals("https://example.com/icon2.png", response2.getIconUrl());
        assertEquals("https://example.com/game2", response2.getPageUrl());
    }

    @Test
    void toResponseList_emptyList_returnsEmptyList() {
        // Arrange
        List<Game> games = Collections.emptyList();

        // Act
        List<GameResponse> result = GameApiMapper.toResponseList(games);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toResponseList_nullList_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> GameApiMapper.toResponseList(null)
        );
    }

    @Test
    void toResponse_validGame_returnsGameResponse() {
        // Arrange
        Game game = new Game(1, "Test Game", true, "https://example.com/icon.png", "https://example.com/game");

        // Act
        GameResponse result = GameApiMapper.toResponse(game);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Game", result.getName());
        assertTrue(result.getEnable());
        assertEquals("https://example.com/icon.png", result.getIconUrl());
        assertEquals("https://example.com/game", result.getPageUrl());
    }

    @Test
    void toResponse_nullGame_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> GameApiMapper.toResponse(null)
        );
    }

    @Test
    void toPageResponse_validPage_returnsPageOfGameResponses() {
        // Arrange
        Game game1 = new Game(1, "Game 1", true, "https://example.com/icon1.png", "https://example.com/game1");
        Game game2 = new Game(2, "Game 2", false, "https://example.com/icon2.png", "https://example.com/game2");
        List<Game> games = Arrays.asList(game1, game2);
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Game> page = new PageImpl<>(games, pageable, games.size());

        // Act
        Page<GameResponse> result = GameApiMapper.toPageResponse(page);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        
        List<GameResponse> content = result.getContent();
        assertEquals(2, content.size());
        
        GameResponse response1 = content.getFirst();
        assertEquals(1, response1.getId());
        assertEquals("Game 1", response1.getName());
        assertTrue(response1.getEnable());
        assertEquals("https://example.com/icon1.png", response1.getIconUrl());
        assertEquals("https://example.com/game1", response1.getPageUrl());
        
        GameResponse response2 = content.get(1);
        assertEquals(2, response2.getId());
        assertEquals("Game 2", response2.getName());
        assertFalse(response2.getEnable());
        assertEquals("https://example.com/icon2.png", response2.getIconUrl());
        assertEquals("https://example.com/game2", response2.getPageUrl());
    }

    @Test
    void toPageResponse_emptyPage_returnsEmptyPage() {
        // Arrange
        List<Game> games = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Game> page = new PageImpl<>(games, pageable, 0);

        // Act
        Page<GameResponse> result = GameApiMapper.toPageResponse(page);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void toPageResponse_nullPage_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> GameApiMapper.toPageResponse(null)
        );
    }

}
