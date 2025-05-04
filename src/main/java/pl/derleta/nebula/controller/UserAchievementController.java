package pl.derleta.nebula.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.derleta.nebula.controller.assembler.UserAchievementModelAssembler;
import pl.derleta.nebula.controller.mapper.UserAchievementApiMapper;
import pl.derleta.nebula.controller.request.UserAchievementFilterRequest;
import pl.derleta.nebula.controller.response.UserAchievementResponse;
import pl.derleta.nebula.exceptions.TokenExpiredException;
import pl.derleta.nebula.service.TokenProvider;
import pl.derleta.nebula.service.UserAchievementProvider;

import java.util.List;
import java.util.Objects;

/**
 * The `UserAchievementController` is a REST controller that handles HTTP requests
 * related to user achievements, including retrieval of achievement details for
 * a specific user, fetching a list of user achievements, and retrieving paginated
 * user achievement data.
 * <p>
 * This controller is secured using JWT token-based authentication, where the token
 * is expected in the "jwtToken" cookie. It validates the token, extracts user
 * information, and ensures that the authenticated user is authorized to access
 * the requested data.
 * <p>
 * The controller provides three main endpoints:
 * 1. Retrieve an achievement for a specific user by user ID and achievement ID.
 * 2. Retrieve a list of achievements for the authenticated user.
 * 3. Retrieve a paginated list of achievements with filtering and sorting capabilities.
 * <p>
 * The controller uses:
 * - `UserAchievementProvider` for fetching user achievement data.
 * - `TokenProvider` for validating and parsing JWT tokens.
 * - `UserAchievementModelAssembler` for mapping achievement data to response models.
 * <p>
 * Cross-origin requests are allowed only from the specified origin.
 * The default media type for the responses is HAL-JSON.
 */
@RestController
@CrossOrigin(origins = {"https://milkyway.local:8555", "https://localhost:3000"}, maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public final class UserAchievementController {

    private final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_PATH = "users/achievements";

    private final UserAchievementProvider provider;
    private final TokenProvider tokenProvider;
    private final UserAchievementModelAssembler modelAssembler;

    /**
     * Retrieves the achievement details for a specific user based on the provided user ID and achievement ID.
     * The request is validated using a JWT token provided in a cookie.
     *
     * @param userId        the ID of the user whose achievement is to be retrieved
     * @param achievementId the ID of the achievement to be retrieved for the user
     * @param accessToken      the JWT token extracted from the "accessToken" cookie used for authentication and validation
     * @return a {@code ResponseEntity} containing the {@link UserAchievementResponse} if the request is valid and successful.
     * Returns a 401 Unauthorized status if the user ID does not match with the one from the token,
     * or a 403 Forbidden status if the provided JWT token is invalid
     * or a 401 Unauthorized status if the provided JWT token is expired
     */
    @GetMapping(value = "/" + DEFAULT_PATH + "/{userId}/{achievementId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<UserAchievementResponse> get(@PathVariable Long userId, @PathVariable Integer achievementId,
                                                       @CookieValue("accessToken") String accessToken) {
        try {
            if (tokenProvider.isValid(accessToken)) {
                if (Objects.equals(userId, tokenProvider.getUserId(accessToken))) {
                    var response = modelAssembler.toModel(
                            provider.get(userId, achievementId));
                    return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (TokenExpiredException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Retrieves a list of user achievement responses associated with the user identified by the JWT token.
     * The method validates the token and fetches data accordingly.
     *
     * @param accessToken the JWT token extracted from the cookie to authenticate and identify the user
     * @return ResponseEntity containing a list of UserAchievementResponse objects if the token is valid;
     * otherwise, returns a ResponseEntity with an HTTP status of 403 (Forbidden)
     * or a 401 Unauthorized status if the provided JWT token is expired
     */
    @GetMapping(value = "/" + DEFAULT_PATH + "/list", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<UserAchievementResponse>> getList(@CookieValue("accessToken") String accessToken) {
        try {
            if (tokenProvider.isValid(accessToken)) {
                long userId = tokenProvider.getUserId(accessToken);
                var list = provider.getList(userId);
                var response = list.stream().map(modelAssembler::toModel).toList();
                return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (TokenExpiredException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Retrieves a paginated list of user achievements based on the provided filtering and sorting criteria.
     *
     * @param page       the page number to retrieve, default is 0 if not provided.
     * @param size       the number of items per page, default is the defined default page size.
     * @param sortBy     the attribute by which the results should be sorted, default is "achievementId".
     * @param sortOrder  the order in which the results should be sorted, the default is "asc" (ascending order).
     * @param level      the achievement level to filter by, optional and defaults to 5 if not provided.
     * @param filterType the type of filter to apply on the level, optional and defaults to "less or equal",
     *                   possible values since 30 mai 2024: greater, less, greater or equal, less or equal, notequal
     * @param accessToken   the JWT token extracted from the cookie header, used for authentication/authorization.
     * @return a {@code ResponseEntity} containing a {@code Page<UserAchievementResponse>} with the filtered and sorted user achievements,
     * or a {@code ResponseEntity} with a forbidden status if the JWT token is invalid.
     * or a 401 Unauthorized status if the provided JWT token is expired
     */
    @GetMapping(value = "/" + DEFAULT_PATH, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Page<UserAchievementResponse>> getPage(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "" + DEFAULT_PAGE_SIZE) int size,
                                                                 @RequestParam(defaultValue = "achievementId") String sortBy,
                                                                 @RequestParam(defaultValue = "asc") String sortOrder,
                                                                 @RequestParam(required = false, defaultValue = "5") Integer level,
                                                                 @RequestParam(required = false, defaultValue = "less or equal") String filterType,
                                                                 @CookieValue("accessToken") String accessToken) {
        try {
            if (tokenProvider.isValid(accessToken)) {
                long userId = tokenProvider.getUserId(accessToken);
                var filterRequest = UserAchievementFilterRequest.builder()
                        .page(page).size(size).sortBy(sortBy).sortOrder(sortOrder)
                        .userId(userId).level(level).filterType(filterType)
                        .build();
                var responsePage = UserAchievementApiMapper.toPageResponse(
                        provider.getPage(filterRequest));
                return new ResponseEntity<>(responsePage, new HttpHeaders(), HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (TokenExpiredException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
