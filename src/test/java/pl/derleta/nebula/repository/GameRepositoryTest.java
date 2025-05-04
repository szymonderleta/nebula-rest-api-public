package pl.derleta.nebula.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.derleta.nebula.domain.entity.GameEntity;
import pl.derleta.nebula.repository.filter.GamesSpecifications;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class GameRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Test
    @Transactional
    void getNextId_shouldReturnNextAvailableId() {
        // Arrange
        GameEntity game = new GameEntity();
        game.setName("Test Game for NextId");
        game.setEnable(true);
        game.setIconUrl("https://example.com/icon.png");
        game.setPageUrl("https://example.com/game");
        
        // Act
        GameEntity savedGame = gameRepository.save(game);
        int nextId = gameRepository.getNextId();
        
        // Assert
        assertEquals(savedGame.getId() + 1, nextId, "Next ID should be one more than the last saved ID");
    }

    @Test
    @Transactional
    void findAll_withSpecification_shouldReturnFilteredResults() {
        // Arrange
        GameEntity game1 = new GameEntity();
        game1.setName("Test Game 1");
        game1.setEnable(true);
        game1.setIconUrl("https://example.com/icon1.png");
        game1.setPageUrl("https://example.com/game1");
        
        GameEntity game2 = new GameEntity();
        game2.setName("Test Game 2");
        game2.setEnable(false);
        game2.setIconUrl("https://example.com/icon2.png");
        game2.setPageUrl("https://example.com/game2");
        
        gameRepository.save(game1);
        gameRepository.save(game2);
        gameRepository.flush();
        entityManager.clear();
        
        Specification<GameEntity> spec = GamesSpecifications.hasAllFilters("Test Game", true);
        Pageable pageable = PageRequest.of(0, 10);
        
        // Act
        Page<GameEntity> result = gameRepository.findAll(spec, pageable);
        
        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.getContent().stream().anyMatch(g -> g.getName().equals("Test Game 1")), 
                "Result should contain game with name 'Test Game 1'");
        assertFalse(result.getContent().stream().anyMatch(g -> g.getName().equals("Test Game 2")), 
                "Result should not contain game with name 'Test Game 2' as it is not enabled");
    }

    @Test
    @Transactional
    void findByName_existingName_shouldReturnGame() {
        // Arrange
        String gameName = "Unique Test Game Name";
        GameEntity game = new GameEntity();
        game.setName(gameName);
        game.setEnable(true);
        game.setIconUrl("https://example.com/icon.png");
        game.setPageUrl("https://example.com/game");
        
        gameRepository.save(game);
        gameRepository.flush();
        entityManager.clear();
        
        // Act
        Optional<GameEntity> result = gameRepository.findByName(gameName);
        
        // Assert
        assertTrue(result.isPresent(), "Game should be found by name");
        assertEquals(gameName, result.get().getName(), "Found game should have the correct name");
    }

    @Test
    @Transactional
    void findByName_nonExistingName_shouldReturnEmptyOptional() {
        // Arrange
        String nonExistingName = "Non Existing Game Name";
        
        // Act
        Optional<GameEntity> result = gameRepository.findByName(nonExistingName);
        
        // Assert
        assertFalse(result.isPresent(), "No game should be found for non-existing name");
    }

    @Test
    @Transactional
    void findByNameOtherThanSelfId_existingNameDifferentId_shouldReturnGame() {
        // Arrange
        String gameName = "Duplicate Test Game Name";
        
        GameEntity game1 = new GameEntity();
        game1.setName(gameName);
        game1.setEnable(true);
        game1.setIconUrl("https://example.com/icon1.png");
        game1.setPageUrl("https://example.com/game1");
        
        GameEntity savedGame1 = gameRepository.save(game1);
        gameRepository.flush();
        entityManager.clear();
        
        // Act
        Optional<GameEntity> result = gameRepository.findByNameOtherThanSelfId(savedGame1.getId() + 1, gameName);
        
        // Assert
        assertTrue(result.isPresent(), "Game should be found by name when using different ID");
        assertEquals(gameName, result.get().getName(), "Found game should have the correct name");
        assertEquals(savedGame1.getId(), result.get().getId(), "Found game should have the correct ID");
    }

    @Test
    @Transactional
    void findByNameOtherThanSelfId_existingNameSameId_shouldReturnEmptyOptional() {
        // Arrange
        String gameName = "Self Test Game Name";
        
        GameEntity game = new GameEntity();
        game.setName(gameName);
        game.setEnable(true);
        game.setIconUrl("https://example.com/icon.png");
        game.setPageUrl("https://example.com/game");
        
        GameEntity savedGame = gameRepository.save(game);
        gameRepository.flush();
        entityManager.clear();
        
        // Act
        Optional<GameEntity> result = gameRepository.findByNameOtherThanSelfId(savedGame.getId(), gameName);
        
        // Assert
        assertFalse(result.isPresent(), "No game should be found when using the same ID");
    }

    @Test
    @Transactional
    void getEnabled_shouldReturnOnlyEnabledGames() {
        // Arrange
        GameEntity enabledGame = new GameEntity();
        enabledGame.setName("Enabled Test Game");
        enabledGame.setEnable(true);
        enabledGame.setIconUrl("https://example.com/icon1.png");
        enabledGame.setPageUrl("https://example.com/game1");
        
        GameEntity disabledGame = new GameEntity();
        disabledGame.setName("Disabled Test Game");
        disabledGame.setEnable(false);
        disabledGame.setIconUrl("https://example.com/icon2.png");
        disabledGame.setPageUrl("https://example.com/game2");
        
        gameRepository.save(enabledGame);
        gameRepository.save(disabledGame);
        gameRepository.flush();
        entityManager.clear();
        
        // Act
        List<GameEntity> enabledGames = gameRepository.getEnabled();
        
        // Assert
        assertNotNull(enabledGames, "Result should not be null");
        assertTrue(enabledGames.stream().anyMatch(g -> g.getName().equals("Enabled Test Game")), 
                "Result should contain the enabled game");
        assertFalse(enabledGames.stream().anyMatch(g -> g.getName().equals("Disabled Test Game")), 
                "Result should not contain the disabled game");
        assertTrue(enabledGames.stream().allMatch(GameEntity::getEnable), 
                "All games in the result should be enabled");
    }

}
