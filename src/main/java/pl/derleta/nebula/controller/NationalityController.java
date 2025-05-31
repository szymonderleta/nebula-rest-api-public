package pl.derleta.nebula.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.derleta.nebula.controller.assembler.NationalityModelAssembler;
import pl.derleta.nebula.controller.response.NationalityResponse;
import pl.derleta.nebula.service.NationalityProvider;

import java.util.Collection;

/**
 * The NationalityController class is a REST controller that provides endpoints to manage
 * nationality resources. It interacts with the service layer to retrieve and handle
 * nationality data and uses model assembling to convert data into HATEOAS-compliant
 * response representations.
 * <p>
 * This controller defines the following endpoints:
 * - Retrieve a specific nationality by ID.
 * - Retrieve a collection of all available nationalities.
 * <p>
 * The controller supports CORS from the origin "<a href="https://milkyway.local:8555">...</a>" with a
 * maximum preflight cache duration of 3600 seconds.
 * <p>
 * The base URI for all endpoints in this controller is "/api/v1".
 */
@RestController
@CrossOrigin(origins = {"https://milkyway.local:8555", "https://localhost:3000"}, maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public final class NationalityController {

    public static final String DEFAULT_PATH = "nationalities";
    private final NationalityProvider provider;
    private final NationalityModelAssembler modelAssembler;

    /**
     * Retrieves the nationality resource representation for the specified ID.
     *
     * @param id the ID of the nationality to retrieve
     * @return a ResponseEntity containing the NationalityResponse object,
     * an empty HttpHeaders object, and an HTTP 200 OK status
     */
    @GetMapping(value = "/" + DEFAULT_PATH + "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<NationalityResponse> get(@PathVariable Integer id) {
        var response = modelAssembler.toModel(
                provider.get(id)
        );
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Retrieves a collection of all available nationalities.
     *
     * @return a ResponseEntity containing a collection of NationalityResponse objects,
     * an empty HttpHeaders object, and an HTTP 200 OK status.
     */
    @GetMapping(value = "/" + DEFAULT_PATH, produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Collection<NationalityResponse>> getAll() {
        var responses = modelAssembler.toCollectionModel(
                provider.getAll()
        );
        return new ResponseEntity<>(responses.getContent(), new HttpHeaders(), HttpStatus.OK);
    }

}
