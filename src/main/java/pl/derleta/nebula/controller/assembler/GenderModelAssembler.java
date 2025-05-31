package pl.derleta.nebula.controller.assembler;

import jakarta.annotation.Nonnull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import pl.derleta.nebula.controller.GenderController;
import pl.derleta.nebula.controller.mapper.GenderApiMapper;
import pl.derleta.nebula.controller.response.GenderResponse;
import pl.derleta.nebula.domain.model.Gender;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * GenderModelAssembler is responsible for converting Gender domain entities
 * into their corresponding GenderResponse representations following the
 * HATEOAS principles, ensuring they include relevant hypermedia links.
 * <p>
 * This class extends RepresentationModelAssemblerSupport, associating the
 * Gender domain model with the GenderResponse resource representation.
 * <p>
 * The primary functionality is adding a self-referential link to each
 * GenderResponse instance, enabling API consumers to access the resource
 * endpoints conveniently and intuitively.
 * <p>
 * The assembler operates as a Spring component, allowing it to be injected
 * wherever needed in the application.
 * <p>
 * Constructor:
 * - Initializes the assembler by associating the GenderController class
 * and GenderResponse class to establish the entity-to-resource mapping.
 * <p>
 * Key Method:
 * - {@code toModel(Gender entity)}: Converts a Gender entity to a GenderResponse,
 * adding a self-referential link to enable easy navigation back to the resource endpoint.
 */
@Component
public final class GenderModelAssembler extends RepresentationModelAssemblerSupport<Gender, GenderResponse> {

    public GenderModelAssembler() {
        super(GenderController.class, GenderResponse.class);
    }

    /**
     * Converts a given Gender entity into its corresponding GenderResponse model representation.
     * This method also adds a self-referential link to the resulting model.
     *
     * @param entity the Gender entity to convert; must not be null
     * @return the GenderResponse model representation with an added self-referential link
     */
    @Override
    @Nonnull
    public GenderResponse toModel(@Nonnull Gender entity) {
        GenderResponse model = GenderApiMapper.toResponse(entity);
        Link selfLink = linkTo(GenderController.class)
                .slash(GenderController.DEFAULT_PATH)
                .slash(model.getId())
                .withSelfRel();
        model.add(selfLink);
        return model;
    }

}
