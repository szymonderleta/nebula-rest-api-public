package pl.derleta.nebula.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.derleta.nebula.controller.assembler.GenderModelAssembler;
import pl.derleta.nebula.controller.response.GenderResponse;
import pl.derleta.nebula.service.GenderProvider;

import java.util.Collection;

/**
 * GenderController provides REST API endpoints for managing and retrieving gender resources.
 * The controller supports fetching a single resource by its unique identifier and retrieving
 * all available gender resources. The resources are presented in a HATEOAS-compliant format.
 * <p>
 * Annotations:
 * - {@code @RestController}: Marks this class as a RESTful web controller.
 * - {@code @CrossOrigin}: Allows cross-origin HTTP requests for specified origins and configurations.
 * - {@code @RequiredArgsConstructor}: Generates a constructor with required arguments based on final fields.
 * - {@code @RequestMapping}: Specifies the base URL for all endpoints in this controller.
 * <p>
 * Fields:
 * - {@code GenderProvider provider}: A service layer dependency responsible for providing gender data.
 * - {@code GenderModelAssembler modelAssembler}: Converts gender entities into HATEOAS-compliant representations.
 * <p>
 * Endpoints:
 * - {@code GET /api/v1/genders/{id}}: Retrieves a specific gender resource by ID.
 * - {@code GET /api/v1/genders}: Retrieves all available gender resources.
 */
@RestController
@CrossOrigin(origins = {"https://milkyway.local:8555", "https://localhost:3000"}, maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public final class GenderController {

    public static final String DEFAULT_PATH = "genders";

    private final GenderProvider provider;
    private final GenderModelAssembler modelAssembler;

    /**
     * Retrieves a specific gender resource by its unique identifier.
     *
     * @param id the unique identifier of the gender resource to retrieve, must not be null
     * @return a response entity containing the {@link GenderResponse} object representing the gender
     * resource, along with HTTP headers and a status code of OK
     */
    @GetMapping(value = "/" + DEFAULT_PATH + "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<GenderResponse> get(@PathVariable Integer id) {
        var response = modelAssembler.toModel(
                provider.get(id)
        );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Retrieves a collection of all available gender resources.
     *
     * @return a response entity containing a collection of {@link GenderResponse} objects
     * representing the genders, along with HTTP headers and a status code of OK.
     */
    @GetMapping(value = "/" + DEFAULT_PATH, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Collection<GenderResponse>> getAll() {
        var responses = modelAssembler.toCollectionModel(
                provider.getAll()
        );
        return new ResponseEntity<>(responses.getContent(), new HttpHeaders(), HttpStatus.OK);
    }

}
