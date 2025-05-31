package pl.derleta.nebula.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.derleta.nebula.domain.entity.GameEntity;
import pl.derleta.nebula.domain.mapper.GameMapper;
import pl.derleta.nebula.domain.model.Game;
import pl.derleta.nebula.exceptions.GameAlreadyExistsException;
import pl.derleta.nebula.exceptions.GameNotFoundException;
import pl.derleta.nebula.repository.GameRepository;
import pl.derleta.nebula.service.GameUpdater;

import java.util.Optional;

/**
 * Service implementation of the GameUpdater interface, responsible for managing
 * the creation, updating, and deletion of game entities in the repository.
 * Maps domain-level Game objects to entity-level GameEntity objects and vice versa.
 */
@AllArgsConstructor
@Service
public class GameUpdaterImpl implements GameUpdater {

    private final GameRepository repository;

    /**
     * Creates a new game record in the repository. If a game with the same id already exists,
     * an exception is thrown. The method converts the provided Game object to a GameEntity,
     * saves it to the repository, and converts the result back to a Game object.
     *
     * @param game the game object containing data to be stored in the repository
     * @return the created Game object after being saved to the repository
     * @throws GameAlreadyExistsException if a game with the given id already exists in the repository, verified by id and name
     */
    @Override
    public Game create(Game game) {
        if (repository.existsById(game.id())) {
            throw new GameAlreadyExistsException("Game with id: " + game.id() + " already exists. Please update the existing record.");
        }
        if (repository.findByName(game.name()).isPresent()) {
            throw new GameAlreadyExistsException("Game with name: " + game.name() + " already exists. Please choose a different name.");
        }
        GameEntity entity = GameMapper.toEntity(game);
        var result = repository.save(entity);
        return GameMapper.toGame(result);
    }

    /**
     * Updates an existing game record in the repository. If the game with the specified id does not exist,
     * or if a game with the provided name already exists, an exception is thrown. The method converts the
     * provided Game object to a GameEntity, updates the corresponding entity in the repository, and
     * converts the result back to a Game object.
     *
     * @param game the game object containing updated data to be applied to the existing record
     * @return the updated Game object after being saved to the repository
     * @throws GameAlreadyExistsException if a game with the given name already exists in the repository
     * @throws GameNotFoundException      if a game with the given id does not exist in the repository
     */
    @Override
    public Game update(Game game) {
        Optional<GameEntity> entityOptional = repository.findById(game.id());
        if (entityOptional.isPresent()) {
            if (repository.findByNameOtherThanSelfId(game.id(), game.name()).isPresent()) {
                throw new GameAlreadyExistsException("Game with name: " + game.name() + " already exists. Please choose a different name.");
            }
            GameEntity entity = GameMapper.toEntity(game);
            var result = repository.save(entity);
            return GameMapper.toGame(result);
        } else {
            throw new GameNotFoundException("Game with id: " + game.id() + " not found");
        }
    }

    /**
     * Deletes a game entity with the specified id from the repository.
     *
     * @param id the unique identifier of the game entity to be deleted
     * @return a ResponseEntity containing a success message if the entity was
     * successfully deleted, or a not found status if the entity does not exist
     */
    @Override
    public ResponseEntity<String> delete(int id) {
        Optional<GameEntity> entityOptional = repository.findById(id);
        if (entityOptional.isPresent()) {
            GameEntity entity = entityOptional.get();
            repository.delete(entity);
            return ResponseEntity.ok("Game with id " + id + " deleted.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
