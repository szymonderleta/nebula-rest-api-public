package pl.derleta.nebula.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.derleta.nebula.controller.assembler.ThemeModelAssembler;
import pl.derleta.nebula.controller.response.ThemeResponse;
import pl.derleta.nebula.service.ThemeProvider;

import java.util.Collection;

/**
 * ThemeController is a REST controller that provides API endpoints for managing theme resources.
 * It allows the retrieval of individual themes as well as a collection of all available themes.
 * <p>
 * Annotations:
 * - {@code @RestController}: Marks this class as a REST controller for handling HTTP requests.
 * - {@code @CrossOrigin}: Enables Cross-Origin Resource Sharing (CORS) for allowed origins.
 * - {@code @RequiredArgsConstructor}: Generates a constructor for fields marked {@code final}.
 * - {@code @RequestMapping}: Maps this controller to a specified base path for all endpoints.
 * <p>
 * Endpoints:
 * - GET /api/v1/themes/{id}: Retrieves a specific theme by its unique identifier.
 * - GET /api/v1/themes: Retrieves a collection of all available themes.
 * <p>
 * Dependencies:
 * - {@code ThemeProvider}: Service for retrieving theme data from the business or persistence layer.
 * - {@code ThemeModelAssembler}: Responsible for converting domain entities into API responses.
 */
@RestController
@CrossOrigin(origins = {"https://milkyway.local:8555", "https://localhost:3000"}, maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public final class ThemeController {

    public static final String DEFAULT_PATH = "themes";

    private final ThemeProvider provider;
    private final ThemeModelAssembler modelAssembler;

    /**
     * Retrieves a theme based on the provided identifier.
     *
     * @param id the unique identifier of the theme to retrieve
     * @return a {@code ResponseEntity} containing a {@code ThemeResponse} object representing
     * the requested theme, along with HTTP headers and a status code of 200 (OK)
     */
    @GetMapping(value = "/" + DEFAULT_PATH + "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<ThemeResponse> get(@PathVariable Integer id) {
        var response = modelAssembler.toModel(
                provider.get(id)
        );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Retrieves a collection of all available themes.
     *
     * @return a {@code ResponseEntity} containing a collection of {@code ThemeResponse}
     * objects representing the available themes, along with HTTP headers and status.
     */
    @GetMapping(value = "/" + DEFAULT_PATH, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Collection<ThemeResponse>> getAll() {
        var responses = modelAssembler.toCollectionModel(
                provider.getAll()
        );
        return new ResponseEntity<>(responses.getContent(), new HttpHeaders(), HttpStatus.OK);
    }

}
