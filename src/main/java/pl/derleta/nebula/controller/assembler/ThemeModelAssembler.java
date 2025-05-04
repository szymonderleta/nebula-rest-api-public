package pl.derleta.nebula.controller.assembler;

import jakarta.annotation.Nonnull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import pl.derleta.nebula.controller.ThemeController;
import pl.derleta.nebula.controller.mapper.ThemeApiMapper;
import pl.derleta.nebula.controller.response.ThemeResponse;
import pl.derleta.nebula.domain.model.Theme;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * ThemeModelAssembler is responsible for converting Theme domain entities
 * into their corresponding ThemeResponse resource representations, following
 * the HATEOAS principles by including relevant hypermedia links.
 * <p>
 * This class extends RepresentationModelAssemblerSupport, associating the
 * Theme domain model with the ThemeResponse resource representation. It acts
 * as an intermediary to ensure consistent entity-to-resource conversion and
 * integration with Spring's HATEOAS library.
 * <p>
 * Functionality:
 * - Converts Theme entities to ThemeResponse models.
 * - Adds a self-referential link to the resulting ThemeResponse, allowing
 * API consumers to access the corresponding resource endpoint directly.
 * <p>
 * Constructor:
 * The assembler is initialized by associating the ThemeController with
 * ThemeResponse to map domain models to their resource representation.
 * <p>
 * Key Method:
 * - {@code toModel(Theme entity)}: Converts a Theme entity into a ThemeResponse,
 * attaching a self-referential link that reflects the resource location.
 * <p>
 * The assembler is marked as a Spring component, making it available for
 * dependency injection where required across the application.
 */
@Component
public final class ThemeModelAssembler extends RepresentationModelAssemblerSupport<Theme, ThemeResponse> {

    public ThemeModelAssembler() {
        super(ThemeController.class, ThemeResponse.class);
    }

    /**
     * Converts a Theme domain entity into a ThemeResponse resource representation.
     * The resulting resource is enhanced with a self-referential hyperlink for HATEOAS compliance.
     *
     * @param entity the Theme domain object to be converted into a resource representation
     * @return a ThemeResponse object containing the converted data and associated hypermedia links
     */
    @Override
    @Nonnull
    public ThemeResponse toModel(@Nonnull Theme entity) {
        ThemeResponse model = ThemeApiMapper.toResponse(entity);
        Link selfLink = linkTo(ThemeController.class)
                .slash(ThemeController.DEFAULT_PATH)
                .slash(model.getId())
                .withSelfRel();
        model.add(selfLink);
        return model;
    }

}
