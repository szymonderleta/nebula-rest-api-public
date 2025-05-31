package pl.derleta.nebula.service.impl;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.controller.request.GameFilterRequest;
import pl.derleta.nebula.domain.entity.GameEntity;
import pl.derleta.nebula.domain.model.Game;
import pl.derleta.nebula.exceptions.GameNotFoundException;
import pl.derleta.nebula.repository.GameRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class GameProviderImplTest {

    @Mock
    private GameRepository repository;

    @InjectMocks
    private GameProviderImpl gameProvider;

    private GameEntity testGameEntity1;
    private Game testGame1;

    @BeforeEach
    void setUp() {
        testGameEntity1 = new GameEntity();
        testGameEntity1.setId(1);
        testGameEntity1.setName("Test Game 1");
        testGameEntity1.setEnable(true);
        testGameEntity1.setIconUrl("https://example.com/icon1.png");
        testGameEntity1.setPageUrl("https://example.com/game1");

        testGame1 = new Game(1, "Test Game 1", true, "https://example.com/icon1.png", "https://example.com/game1");
    }

    @SuppressWarnings("unchecked")
    private Specification<GameEntity> anySpecification() {
        return any(Specification.class);
    }

    @Test
    void get_shouldReturnGame_whenValidIdProvided() {
        // Arrange
        when(repository.findById(1)).thenReturn(Optional.of(testGameEntity1));

        // Act
        Game result = gameProvider.get(1);

        // Assert
        assertNotNull(result);
        assertEquals(testGame1.id(), result.id());
        assertEquals(testGame1.name(), result.name());
        assertEquals(testGame1.enable(), result.enable());
        assertEquals(testGame1.iconUrl(), result.iconUrl());
        assertEquals(testGame1.pageUrl(), result.pageUrl());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void get_shouldThrowIllegalArgumentException_whenNullIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Integer nullId = null;
            gameProvider.get(nullId);
        });
        assertEquals("Game ID cannot be null", exception.getMessage());
        verify(repository, never()).findById(anyInt());
    }

    @Test
    void get_shouldThrowGameNotFoundException_whenGameNotFound() {
        // Arrange
        when(repository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> gameProvider.get(999));
        assertEquals("Game with id: 999 not found", exception.getMessage());
        verify(repository, times(1)).findById(999);
    }

    @Test
    void get_shouldReturnPageOfGames_whenFilterRequestProvided() {
        // Arrange
        GameFilterRequest request = GameFilterRequest.builder()
                .page(0)
                .size(10)
                .sortBy("name")
                .sortOrder("ASC")
                .name("Test")
                .enable(true)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        List<GameEntity> gameEntities = Collections.singletonList(testGameEntity1);
        Page<GameEntity> entityPage = new PageImpl<>(gameEntities, pageable, 1);

        when(repository.findAll(anySpecification(), any(Pageable.class))).thenReturn(entityPage);

        // Act
        Page<Game> result = gameProvider.get(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testGame1.id(), result.getContent().getFirst().id());
        assertEquals(testGame1.name(), result.getContent().getFirst().name());
        verify(repository, times(1)).findAll(anySpecification(), any(Pageable.class));
    }

    @Test
    void get_shouldReturnEmptyPage_whenNoGamesMatchFilter() {
        // Arrange
        GameFilterRequest request = GameFilterRequest.builder()
                .page(0)
                .size(10)
                .sortBy("name")
                .sortOrder("ASC")
                .name("NonExistent")
                .enable(true)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<GameEntity> entityPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(repository.findAll(anySpecification(), any(Pageable.class))).thenReturn(entityPage);

        // Act
        Page<Game> result = gameProvider.get(request);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(repository, times(1)).findAll(anySpecification(), any(Pageable.class));
    }

    @Test
    void getNextId_shouldReturnNextId() {
        // Arrange
        when(repository.getNextId()).thenReturn(3);

        // Act
        int result = gameProvider.getNextId();

        // Assert
        assertEquals(3, result);
        verify(repository, times(1)).getNextId();
    }

    @Test
    void getEnabled_shouldReturnListOfEnabledGames() {
        // Arrange
        List<GameEntity> enabledGameEntities = Collections.singletonList(testGameEntity1);
        when(repository.getEnabled()).thenReturn(enabledGameEntities);

        // Act
        List<Game> result = gameProvider.getEnabled();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testGame1.id(), result.getFirst().id());
        assertEquals(testGame1.name(), result.getFirst().name());
        assertTrue(result.getFirst().enable());
        verify(repository, times(1)).getEnabled();
    }

    @Test
    void getEnabled_shouldReturnEmptyList_whenNoEnabledGames() {
        // Arrange
        when(repository.getEnabled()).thenReturn(Collections.emptyList());

        // Act
        List<Game> result = gameProvider.getEnabled();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).getEnabled();
    }

}
