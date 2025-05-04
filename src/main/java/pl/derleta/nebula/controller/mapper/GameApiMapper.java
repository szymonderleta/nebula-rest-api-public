package pl.derleta.nebula.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import pl.derleta.nebula.controller.response.GameResponse;
import pl.derleta.nebula.domain.model.Game;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class that maps Game domain objects to corresponding GameResponse DTOs.
 * This class is designed to be used as a static utility and cannot be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GameApiMapper {

    /**
     * Converts a list of Game objects into a list of GameResponse objects.
     * Each Game object in the input list is transformed into a corresponding GameResponse
     * object using the GameApiMapper::toResponse method.
     *
     * @param items the list of Game objects to be converted, where each object contains attributes
     *              such as id, name, enable status, icon URL, and page URL
     * @return a list of GameResponse objects, each containing the transformed data corresponding
     * to the Game objects in the input list
     */
    public static List<GameResponse> toResponseList(List<Game> items) {
        return items.stream()
                .map(GameApiMapper::toResponse)
                .toList();
    }

    /**
     * Converts a Game object into a GameResponse object.
     *
     * @param game the Game object to be converted, containing the attributes such as id, name,
     *             enable status, icon URL, and page URL
     * @return a GameResponse object containing the transformed data from the given Game object,
     * including id, name, enable status, icon URL, and page URL
     */
    public static GameResponse toResponse(final Game game) {
        return GameResponse.builder()
                .id(game.id())
                .name(game.name())
                .enable(game.enable())
                .iconUrl(game.iconUrl())
                .pageUrl(game.pageUrl())
                .build();
    }

    /**
     * Converts a Page object containing Game entities into a Page object containing GameResponse DTOs.
     * Each Game object in the input Page is transformed into a GameResponse using the GameApiMapper::toResponse method.
     *
     * @param page the Page object containing entities of type Game
     * @return a Page object containing entities of type GameResponse, with pagination and total element count preserved
     */
    public static Page<GameResponse> toPageResponse(Page<Game> page) {
        List<GameResponse> gameResponses = page.getContent().stream()
                .map(GameApiMapper::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(gameResponses, page.getPageable(), page.getTotalElements());
    }

}
