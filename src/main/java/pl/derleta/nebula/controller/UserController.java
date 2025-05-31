package pl.derleta.nebula.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.derleta.nebula.controller.assembler.UserModelAssembler;
import pl.derleta.nebula.controller.mapper.UserSettingsApiMapper;
import pl.derleta.nebula.controller.request.ProfileUpdateRequest;
import pl.derleta.nebula.controller.request.UserSettingsRequest;
import pl.derleta.nebula.controller.response.NebulaUserResponse;
import pl.derleta.nebula.controller.response.Response;
import pl.derleta.nebula.domain.mapper.UserSettingsMapper;
import pl.derleta.nebula.domain.model.UserSettings;
import pl.derleta.nebula.service.TokenProvider;
import pl.derleta.nebula.service.UserProvider;
import pl.derleta.nebula.service.UserUpdater;

/**
 * The UserController class is a REST controller handling API endpoints related to user data,
 * profile updates, and settings. It performs authentication using JWT tokens
 * provided as cookies and ensures that operations are authorized for the specified users.
 * <p>
 * This controller includes endpoints for:
 * - Retrieving user data based on a valid JWT token.
 * - Updating user profiles if the JWT token is valid and authorized.
 * - Updating user settings if the JWT token is valid and associated with the correct user.
 * <p>
 * Cross-origin requests are allowed from the specified origin with a defined max age.
 */
@RestController
@CrossOrigin(origins = {"https://milkyway.local:8555", "https://localhost:3000"}, maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public final class UserController {

    public static final String DEFAULT_PATH = "users";

    private final UserProvider provider;
    private final UserUpdater updater;
    private final TokenProvider tokenProvider;
    private final UserModelAssembler modelAssembler;

    /**
     * Retrieves the user data corresponding to the provided JWT token.
     * If the token is valid, the user data is retrieved and returned as a response.
     * If the token is invalid, a forbidden status response is returned.
     *
     * @param accessToken the JWT token used for authentication, passed as a cookie value
     * @return a ResponseEntity containing the user data in a NebulaUserResponse object if the token is valid,
     * or a ResponseEntity with a forbidden status if the token is invalid
     */
    @GetMapping(value = "/" + DEFAULT_PATH, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<NebulaUserResponse> getUserData(@CookieValue("accessToken") String accessToken) {
        if (tokenProvider.isValid(accessToken)) {
            long userId = tokenProvider.getUserId(accessToken);
            var response = modelAssembler.toModel(
                    provider.get(userId)
            );
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    /**
     * Updates the user profile with the specified data. Authentication is performed
     * using the provided JWT token, and the profile update is executed only if the
     * token is valid for the specified user.
     *
     * @param accessToken    the JWT token used for authentication, passed as a cookie value
     * @param profileData the profile data to be updated, containing the user's profile information
     * @return a ResponseEntity containing the updated profile data in the
     * response object if the token is valid, or a ResponseEntity with a
     * forbidden status if the token is invalid
     */
    @PatchMapping(value = "/" + DEFAULT_PATH + "/profile", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Response> updateUserProfile(@CookieValue("accessToken") String accessToken,
                                                      @RequestBody ProfileUpdateRequest profileData) {
        if (tokenProvider.isValid(accessToken, profileData.getUserId())) {
            var response = modelAssembler.toModel(
                    updater.updateProfile(profileData)
            );
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    /**
     * Updates the user settings with the specified data. Authentication is performed
     * using the provided JWT token, and the settings update is executed only if the
     * token is valid for the specified user.
     *
     * @param accessToken     the JWT token used for authentication, passed as a cookie value
     * @param request the new user settings to be updated, containing the user's settings information
     * @return a ResponseEntity containing the updated settings data in the response object if the token is valid,
     * or a ResponseEntity with a forbidden status if the token is invalid
     */
    @PutMapping(value = "/" + DEFAULT_PATH + "/settings", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Response> updateUserSettings(@CookieValue("accessToken") String accessToken,
                                                       @RequestBody UserSettingsRequest request) {
        if (tokenProvider.isValid(accessToken, request.userId())) {
            UserSettings userSettings = UserSettingsMapper.requestToSettings(request);
            var response = UserSettingsApiMapper.toResponse(
                    updater.updateSettings(userSettings)
            );
            return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

}
