package pl.derleta.nebula.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.derleta.nebula.controller.assembler.TokenModelAssembler;
import pl.derleta.nebula.controller.response.AccessResponse;
import pl.derleta.nebula.controller.response.Response;
import pl.derleta.nebula.controller.response.TokenDataResponse;
import pl.derleta.nebula.domain.rest.Role;
import pl.derleta.nebula.domain.types.TokenResponseType;
import pl.derleta.nebula.exceptions.TokenExpiredException;
import pl.derleta.nebula.service.TokenProvider;
import pl.derleta.nebula.service.TokenUpdater;

import java.util.Map;
import java.util.Set;

/**
 * A REST controller for managing operations related to JWT tokens.
 * Provides endpoints to retrieve token details, validate tokens,
 * and access user-related information based on the token.
 */
@RestController
@CrossOrigin(origins = {"https://milkyway.local:8555", "https://localhost:3000"}, maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public final class TokenController {

    public static final String DEFAULT_PATH = "token";
    private final TokenProvider provider;
    private final TokenUpdater updater;
    private final TokenModelAssembler modelAssembler;

    /**
     * Retrieves token data based on the provided JWT token stored as a cookie.
     * The response is converted into a HATEOAS-compliant {@link TokenDataResponse} representation.
     *
     * @param accessToken the JWT token extracted from the "accessToken" cookie
     * @return a {@link ResponseEntity} containing the token data wrapped in a {@link TokenDataResponse},
     * with HTTP status 200 (OK)
     * or an HTTP 401 (Unauthorized) status if the token has expired with an empty response body.
     */
    @GetMapping(value = "/" + DEFAULT_PATH, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<TokenDataResponse> get(@CookieValue("accessToken") String accessToken) {
        if (provider.isValid(accessToken)) {
            var response = modelAssembler.toModel(
                    provider.getTokenData(accessToken)
            );
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    /**
     * Validates the provided JWT token extracted from the "accessToken" cookie.
     *
     * @param accessToken the JWT token extracted from the "accessToken" cookie
     * @return a {@link ResponseEntity} containing a {@link Boolean} indicating whether the token is valid
     * or an HTTP 401 (Unauthorized) status if the token has expired with false as the response body.
     */
    @GetMapping(value = "/" + DEFAULT_PATH + "/valid", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Boolean> isValid(@CookieValue("accessToken") String accessToken) {
        var result = provider.isValid(accessToken);
        if (result) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    /**
     * Retrieves the set of roles associated with the provided JWT token.
     *
     * @param accessToken the JWT token extracted from the "accessToken" cookie
     * @return a {@link ResponseEntity} containing a {@link Set} of {@link Role} objects if the token is valid,
     * or an HTTP 400 (Bad Request) status if the token is invalid
     * or an HTTP 401 (Unauthorized) status if the token has expired with an empty set of roles.
     */
    @GetMapping(value = "/" + DEFAULT_PATH + "/roles", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Set<Role>> getRoles(@CookieValue("accessToken") String accessToken) {
        var valid = provider.isValid(accessToken);
        if (valid) {
            Set<Role> roles = provider.getRoles(accessToken);
            return ResponseEntity.ok(roles);
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * Retrieves the email address associated with the provided JWT token.
     *
     * @param accessToken the JWT token extracted from the "accessToken" cookie
     * @return a {@link ResponseEntity} containing the email address as a {@link String} if the token is valid,
     * or an HTTP 400 (Bad Request) status if the token is invalid
     * or an HTTP 401 (Unauthorized) status if the token has expired.
     */
    @GetMapping(value = "/" + DEFAULT_PATH + "/email", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> getEmail(@CookieValue("accessToken") String accessToken) {
        var valid = provider.isValid(accessToken);
        if (valid) {
            String email = provider.getEmail(accessToken);
            return ResponseEntity.ok(email);
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * Retrieves the user ID associated with the provided JWT token.
     *
     * @param accessToken the JWT token extracted from the "accessToken" cookie
     * @return a {@link ResponseEntity} containing the user ID as a {@link Long} if the token is valid,
     * or an HTTP 400 (Bad Request) status if the token is invalid
     * or an HTTP 401 (Unauthorized) status if the token has expired with body -1L.
     */
    @GetMapping(value = "/" + DEFAULT_PATH + "/id", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Long> getId(@CookieValue("accessToken") String accessToken) {
        var valid = provider.isValid(accessToken);
        if (valid) {
            Long id = provider.getUserId(accessToken);
            return ResponseEntity.ok(id);
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * Refreshes the access token using the refresh token provided in the "refreshToken" cookie.
     * <p>
     * This endpoint accepts the refresh token from the client's cookies, validates it, and, if the token
     * is valid, generates a new access token and returns it in the response.
     * </p>
     * <p>
     * If the refresh token is invalid or expired, a `BAD_REQUEST` or `UNAUTHORIZED` response is returned.
     *
     * @param refreshToken the refresh token extracted from the "refreshToken" cookie.
     *                     This token must not be null or empty. It is validated before generating the new access token.
     * @return a `ResponseEntity` containing the new access token in the body if the token is valid, or
     * an error response (`BAD_REQUEST` if invalid, `UNAUTHORIZED` if expired).
     * @throws TokenExpiredException if the refresh token has expired.
     */
    @PostMapping(value = "/" + DEFAULT_PATH + "/refresh/access", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Response> refreshAccess(@CookieValue("refreshToken") String refreshToken) {
        var valid = provider.isValid(refreshToken);
        if (valid) {
            var response = updater.refreshAccess(refreshToken);
            return getResponseForRefreshAccess(response);
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    /**
     * Builds a {@link ResponseEntity} containing refreshed access and refresh tokens as HTTP headers.
     * <p>
     * If the provided {@link Response} is an instance of {@link AccessResponse}, the method retrieves
     * the associated cookies and includes them as {@code Set-Cookie} headers in the response.
     * Otherwise, it returns a 400 Bad Request response.
     *
     * @param response the response object that may contain refreshed token cookie headers
     * @return a {@link ResponseEntity} with appropriate {@code Set-Cookie} headers if the response is valid,
     * or a 400 Bad Request response if the input is not an {@code AccessResponse}
     */
    private ResponseEntity<Response> getResponseForRefreshAccess(Response response) {
        if (response instanceof AccessResponse instance) {
            Map<String, String> cookies = instance.getCookiesHeaders();
            return ResponseEntity.ok()
                    .header("Set-Cookie", cookies.get("accessToken"))
                    .header("Set-Cookie", cookies.get("refreshToken"))
                    .body(instance);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

}
