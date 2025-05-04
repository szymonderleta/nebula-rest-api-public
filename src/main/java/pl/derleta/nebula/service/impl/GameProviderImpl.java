package pl.derleta.nebula.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import pl.derleta.nebula.controller.request.GameFilterRequest;
import pl.derleta.nebula.domain.entity.GameEntity;
import pl.derleta.nebula.domain.mapper.GameMapper;
import pl.derleta.nebula.domain.model.Game;
import pl.derleta.nebula.exceptions.GameNotFoundException;
import pl.derleta.nebula.repository.GameRepository;
import pl.derleta.nebula.repository.filter.GamesSpecifications;
import pl.derleta.nebula.service.GameProvider;

import java.util.List;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

/**
 * Implementation of the GameProvider interface to manage Game-related functionalities.
 * This class is annotated with @Service, making it a Spring-managed bean.
 */
@AllArgsConstructor
@Service
public class GameProviderImpl implements GameProvider {

    private final GameRepository repository;

    /**
     * Retrieves a Game object by its unique identifier.
     *
     * @param id the unique identifier of the game to be retrieved
     * @return the Game object corresponding to the provided id
     * @throws IllegalArgumentException if the provided id is null
     * @throws GameNotFoundException    if a game with the given id is not found
     */
    @Override
    public Game get(final Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Game ID cannot be null");
        }
        var entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new GameNotFoundException("Game with id: " + id + " not found");
        }
        return GameMapper.toGame(entity.get());
    }

    /**
     * Retrieves a paginated list of games based on the provided filtering and sorting parameters.
     *
     * @param request the filter request containing pagination, sorting, and filtering criteria
     * @return a paginated list of games that match the specified criteria
     */
    @Override
    public Page<Game> get(final GameFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortOrder()), request.getSortBy()));
        Specification<GameEntity> spec = GamesSpecifications.hasAllFilters(request.getName(), request.getEnable());
        return getPage(repository.findAll(spec, pageable));
    }

    /**
     * Converts a page of GameEntity objects to a page of Game objects.
     *
     * @param entitiesPage the page of GameEntity objects to be converted
     * @return a page of Game objects
     */
    private Page<Game> getPage(Page<GameEntity> entitiesPage) {
        List<Game> collection = entitiesPage
                .stream()
                .map(GameMapper::toGame)
                .collect(Collectors.toList());
        LongSupplier totalElements = entitiesPage::getTotalElements;
        return PageableExecutionUtils.getPage(collection, entitiesPage.getPageable(), totalElements);
    }

    /**
     * Retrieves the next available ID for a game entity by incrementing the maximum current ID from the repository.
     *
     * @return the next available ID as an integer
     */
    @Override
    public int getNextId() {
        return repository.getNextId();
    }

    /**
     * Retrieves a list of enabled games by transforming enabled GameEntity objects
     * from the repository into Game objects.
     *
     * @return a list of enabled Game objects
     */
    @Override
    public List<Game> getEnabled() {
        return repository.getEnabled()
                .stream()
                .map(GameMapper::toGame)
                .collect(Collectors.toList());
    }
}
