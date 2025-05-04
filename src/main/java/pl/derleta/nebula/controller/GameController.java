package pl.derleta.nebula.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.derleta.nebula.controller.mapper.GameApiMapper;
import pl.derleta.nebula.controller.request.GameFilterRequest;
import pl.derleta.nebula.controller.request.GameNewRequest;
import pl.derleta.nebula.controller.response.GameResponse;
import pl.derleta.nebula.domain.mapper.GameMapper;
import pl.derleta.nebula.domain.model.Game;
import pl.derleta.nebula.service.AuthorizationService;
import pl.derleta.nebula.service.GameProvider;
import pl.derleta.nebula.service.GameUpdater;

import java.util.List;

/**
 * GameController is a REST controller that provides API endpoints for managing game entities.
 * It supports operations such as retrieving individual games, paginating through games,
 * adding new games, updating existing games, and deleting games. The controller ensures
 * proper authorization using JWT tokens stored in cookies.
 */
@RestController
@CrossOrigin(origins = {"https://milkyway.local:8555", "https://localhost:3000"}, maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public final class GameController {

    private final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_PATH = "games";

    private final GameProvider provider;
    private final GameUpdater updater;
    private final AuthorizationService authorizationService;


    /**
     * Retrieves a game entity by its ID.
     *
     * @param id the unique identifier of the game to be retrieved
     * @return a {@link ResponseEntity} containing the {@link GameResponse} with the game details
     */
    @GetMapping("/" + DEFAULT_PATH + "/{id}")
    public ResponseEntity<GameResponse> get(@PathVariable Integer id) {
        var responses = GameApiMapper.toResponse(provider.get(id));
        return new ResponseEntity<>(responses, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Retrieves a paginated list of games based on the provided filters and sorting options.
     *
     * @param page      the current page number to retrieve, defaults to 0
     * @param size      the number of items per page, defaults to the configured default page size
     * @param sortBy    the property used to sort the results, defaults to 'name'
     * @param sortOrder the sort order, either 'asc' for ascending or 'desc' for descending, defaults to 'asc'
     * @param name      a filter parameter to search for games by name, optional
     * @param enable    a filter parameter to include only enabled or disabled games, defaults to true
     * @return a {@link ResponseEntity} containing a {@link Page} of {@link GameResponse} objects representing the paginated result
     */
    @GetMapping("/" + DEFAULT_PATH)
    public ResponseEntity<Page<GameResponse>> getPage(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
                                                      @RequestParam(defaultValue = "name") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String sortOrder,
                                                      @RequestParam(required = false, defaultValue = "") String name,
                                                      @RequestParam(required = false, defaultValue = "true") Boolean enable) {
        var filterRequest = GameFilterRequest.builder()
                .page(page).size(size).sortBy(sortBy).sortOrder(sortOrder)
                .name(name).enable(enable)
                .build();
        var responsePage = GameApiMapper.toPageResponse(
                provider.get(filterRequest));
        return new ResponseEntity<>(responsePage, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Retrieves a list of enabled game responses.
     *
     * @return a ResponseEntity containing a list of GameResponse objects representing the enabled games,
     *         along with HTTP headers and HTTP status code OK.
     */
    @GetMapping("/" + DEFAULT_PATH + "/enabled")
    public ResponseEntity<List<GameResponse>> getEnabled() {
        var response = GameApiMapper.toResponseList(provider.getEnabled());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Adds a new game to the system. Requires an authenticated user with the ADMIN role.
     *
     * @param accessToken The JWT token extracted from the client's cookies for authentication and authorization purposes.
     * @param request  The request body containing the details of the new game to be added.
     * @return A ResponseEntity containing the response object for the newly added game and the corresponding HTTP status code.
     */
    @PostMapping("/" + DEFAULT_PATH)
    public ResponseEntity<GameResponse> add(@CookieValue("accessToken") String accessToken, @RequestBody GameNewRequest request) {
        if (authorizationService.notContainsAdminRole(accessToken))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        int id = provider.getNextId();
        final Game game = GameMapper.toGame(id, request.name(), request.enable(), request.iconUrl(), request.pageUrl());
        var response = GameApiMapper.toResponse(
                updater.create(game)
        );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Updates an existing game identified by its ID with the provided details.
     *
     * @param accessToken the JWT token extracted from the cookie to validate the user's authentication and authorization
     * @param id       the ID of the game to be updated
     * @param request  the new data for the game, encapsulated in the request object
     * @return ResponseEntity containing the updated game details in the response body if the operation is successful,
     * or a FORBIDDEN status if the user lacks the required permissions
     */
    @PutMapping("/" + DEFAULT_PATH + "/{id}")
    public ResponseEntity<GameResponse> update(@CookieValue("accessToken") String accessToken,
                                               @PathVariable Integer id,
                                               @RequestBody GameNewRequest request) {
        if (authorizationService.notContainsAdminRole(accessToken))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        final Game game = GameMapper.toGame(id, request.name(), request.enable(), request.iconUrl(), request.pageUrl());
        var response = GameApiMapper.toResponse(
                updater.update(game)
        );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Handles the HTTP DELETE request to delete a resource by its ID.
     *
     * @param accessToken the JWT token extracted from the request's cookies for authentication
     * @param id       the ID of the resource to be deleted
     * @return a ResponseEntity containing the HTTP status code and optional body
     */
    @DeleteMapping("/" + DEFAULT_PATH + "/{id}")
    public ResponseEntity<String> delete(@CookieValue("accessToken") String accessToken, @PathVariable Integer id) {
        if (authorizationService.notContainsAdminRole(accessToken))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        return updater.delete(id);
    }

}
