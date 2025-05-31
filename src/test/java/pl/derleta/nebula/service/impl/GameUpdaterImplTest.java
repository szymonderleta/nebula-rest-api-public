package pl.derleta.nebula.service.impl;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.domain.entity.GameEntity;
import pl.derleta.nebula.domain.model.Game;
import pl.derleta.nebula.exceptions.GameAlreadyExistsException;
import pl.derleta.nebula.exceptions.GameNotFoundException;
import pl.derleta.nebula.repository.GameRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class GameUpdaterImplTest {

    @Mock
    private GameRepository repository;

    @InjectMocks
    private GameUpdaterImpl gameUpdater;

    @Test
    void create_shouldReturnCreatedGame_whenGameDoesNotExist() {
        // Arrange
        Game game = new Game(1, "Test Game", true, "https://icon.com", "https://page.com");
        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(1);
        gameEntity.setName("Test Game");
        gameEntity.setEnable(true);
        gameEntity.setIconUrl("https://icon.com");
        gameEntity.setPageUrl("https://page.com");

        GameEntity savedEntity = new GameEntity();
        savedEntity.setId(1);
        savedEntity.setName("Test Game");
        savedEntity.setEnable(true);
        savedEntity.setIconUrl("https://icon.com");
        savedEntity.setPageUrl("https://page.com");

        when(repository.existsById(1)).thenReturn(false);
        when(repository.findByName("Test Game")).thenReturn(Optional.empty());
        when(repository.save(any(GameEntity.class))).thenReturn(savedEntity);

        // Act
        Game result = gameUpdater.create(game);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Test Game", result.name());
        assertEquals("https://icon.com", result.iconUrl());
        assertEquals("https://page.com", result.pageUrl());
        assertTrue(result.enable());
        verify(repository, times(1)).existsById(1);
        verify(repository, times(1)).findByName("Test Game");
        verify(repository, times(1)).save(any(GameEntity.class));
    }

    @Test
    void create_shouldThrowGameAlreadyExistsException_whenGameWithSameIdExists() {
        // Arrange
        Game game = new Game(1, "Test Game", true, "https://icon.com", "https://page.com");
        when(repository.existsById(1)).thenReturn(true);

        // Act & Assert
        GameAlreadyExistsException exception = assertThrows(GameAlreadyExistsException.class, () -> gameUpdater.create(game));
        assertEquals("Game with id: 1 already exists. Please update the existing record.", exception.getMessage());
        verify(repository, times(1)).existsById(1);
        verify(repository, never()).findByName(anyString());
        verify(repository, never()).save(any(GameEntity.class));
    }

    @Test
    void create_shouldThrowGameAlreadyExistsException_whenGameWithSameNameExists() {
        // Arrange
        Game game = new Game(1, "Test Game", true, "https://icon.com", "https://page.com");
        GameEntity existingGame = new GameEntity();
        existingGame.setId(2);
        existingGame.setName("Test Game");
        existingGame.setEnable(true);
        existingGame.setIconUrl("https://other-icon.com");
        existingGame.setPageUrl("https://other-page.com");

        when(repository.existsById(1)).thenReturn(false);
        when(repository.findByName("Test Game")).thenReturn(Optional.of(existingGame));

        // Act & Assert
        GameAlreadyExistsException exception = assertThrows(GameAlreadyExistsException.class, () -> gameUpdater.create(game));
        assertEquals("Game with name: Test Game already exists. Please choose a different name.", exception.getMessage());
        verify(repository, times(1)).existsById(1);
        verify(repository, times(1)).findByName("Test Game");
        verify(repository, never()).save(any(GameEntity.class));
    }

    @Test
    void update_shouldReturnUpdatedGame_whenGameExists() {
        // Arrange
        Game game = new Game(1, "Updated Game", true, "https://icon.com", "https://page.com");
        GameEntity existingEntity = new GameEntity();
        existingEntity.setId(1);
        existingEntity.setName("Test Game");
        existingEntity.setEnable(true);
        existingEntity.setIconUrl("https://icon.com");
        existingEntity.setPageUrl("https://page.com");

        GameEntity updatedEntity = new GameEntity();
        updatedEntity.setId(1);
        updatedEntity.setName("Updated Game");
        updatedEntity.setEnable(true);
        updatedEntity.setIconUrl("https://icon.com");
        updatedEntity.setPageUrl("https://page.com");

        when(repository.findById(1)).thenReturn(Optional.of(existingEntity));
        when(repository.findByNameOtherThanSelfId(1, "Updated Game")).thenReturn(Optional.empty());
        when(repository.save(any(GameEntity.class))).thenReturn(updatedEntity);

        // Act
        Game result = gameUpdater.update(game);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Updated Game", result.name());
        assertEquals("https://icon.com", result.iconUrl());
        assertEquals("https://page.com", result.pageUrl());
        assertTrue(result.enable());
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).findByNameOtherThanSelfId(1, "Updated Game");
        verify(repository, times(1)).save(any(GameEntity.class));
    }

    @Test
    void update_shouldThrowGameNotFoundException_whenGameDoesNotExist() {
        // Arrange
        Game game = new Game(999, "Test Game", true, "https://icon.com", "https://page.com");
        when(repository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> gameUpdater.update(game));
        assertEquals("Game with id: 999 not found", exception.getMessage());
        verify(repository, times(1)).findById(999);
        verify(repository, never()).findByNameOtherThanSelfId(anyInt(), anyString());
        verify(repository, never()).save(any(GameEntity.class));
    }

    @Test
    void update_shouldThrowGameAlreadyExistsException_whenGameWithSameNameExists() {
        // Arrange
        Game game = new Game(1, "Updated Game", true, "https://icon.com", "https://page.com");
        GameEntity existingEntity = new GameEntity();
        existingEntity.setId(1);
        existingEntity.setName("Test Game");
        existingEntity.setEnable(true);
        existingEntity.setIconUrl("https://icon.com");
        existingEntity.setPageUrl("https://page.com");

        GameEntity otherEntity = new GameEntity();
        otherEntity.setId(2);
        otherEntity.setName("Updated Game");
        otherEntity.setEnable(true);
        otherEntity.setIconUrl("https://other-icon.com");
        otherEntity.setPageUrl("https://other-page.com");

        when(repository.findById(1)).thenReturn(Optional.of(existingEntity));
        when(repository.findByNameOtherThanSelfId(1, "Updated Game")).thenReturn(Optional.of(otherEntity));

        // Act & Assert
        GameAlreadyExistsException exception = assertThrows(GameAlreadyExistsException.class, () -> gameUpdater.update(game));
        assertEquals("Game with name: Updated Game already exists. Please choose a different name.", exception.getMessage());
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).findByNameOtherThanSelfId(1, "Updated Game");
        verify(repository, never()).save(any(GameEntity.class));
    }

    @Test
    void delete_shouldReturnOkResponse_whenGameExists() {
        // Arrange
        int gameId = 1;
        GameEntity existingEntity = new GameEntity();
        existingEntity.setId(1);
        existingEntity.setName("Test Game");
        existingEntity.setEnable(true);
        existingEntity.setIconUrl("https://icon.com");
        existingEntity.setPageUrl("https://page.com");

        when(repository.findById(gameId)).thenReturn(Optional.of(existingEntity));
        doNothing().when(repository).delete(existingEntity);

        // Act
        ResponseEntity<String> response = gameUpdater.delete(gameId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Game with id 1 deleted.", response.getBody());
        verify(repository, times(1)).findById(gameId);
        verify(repository, times(1)).delete(existingEntity);
    }

    @Test
    void delete_shouldReturnNotFoundResponse_whenGameDoesNotExist() {
        // Arrange
        int gameId = 999;
        when(repository.findById(gameId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = gameUpdater.delete(gameId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(repository, times(1)).findById(gameId);
        verify(repository, never()).delete(any(GameEntity.class));
    }

}
