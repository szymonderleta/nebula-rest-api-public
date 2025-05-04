package pl.derleta.nebula.controller.assembler;

import jakarta.annotation.Nonnull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import pl.derleta.nebula.controller.NationalityController;
import pl.derleta.nebula.controller.mapper.NationalityApiMapper;
import pl.derleta.nebula.controller.response.NationalityResponse;
import pl.derleta.nebula.domain.model.Nationality;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * NationalityModelAssembler is responsible for converting Nationality domain entities
 * into their corresponding NationalityResponse representations following the
 * HATEOAS principles, ensuring they include relevant hypermedia links.
 * <p>
 * This class extends RepresentationModelAssemblerSupport, establishing the mapping
 * between the Nationality domain model and the NationalityResponse resource representation.
 * <p>
 * The main purpose of this assembler is to provide a way to encapsulate the conversion
 * logic and enrich the resource representation with hypermedia links, particularly a
 * self-referential link for easier navigation to the resource endpoint.
 * <p>
 * Constructor:
 * - Initializes the assembler by associating the NationalityController class
 * and NationalityResponse class to define the entity-to-resource mapping.
 * <p>
 * Key Method:
 * - {@code toModel(Nationality entity)}: Converts a Nationality entity to a NationalityResponse,
 * adding a self-referential link to provide navigational access to the specific resource endpoint.
 * <p>
 * This component operates as a Spring-managed bean, making it injectable across the application
 * wherever model conversion from Nationality to NationalityResponse is required.
 */
@Component
public final class NationalityModelAssembler extends RepresentationModelAssemblerSupport<Nationality, NationalityResponse> {

    public NationalityModelAssembler() {
        super(NationalityController.class, NationalityResponse.class);
    }

    /**
     * Converts a Nationality domain entity into a NationalityResponse resource representation.
     * The resulting resource is enriched with a self-referential hyperlink to support HATEOAS compliance.
     *
     * @param entity the Nationality domain object to be converted into a resource representation
     * @return a NationalityResponse object containing the converted data and associated hypermedia links
     */
    @Override
    @Nonnull
    public NationalityResponse toModel(@Nonnull Nationality entity) {
        NationalityResponse model = NationalityApiMapper.toResponse(entity);
        Link selfLink = linkTo(NationalityController.class)
                .slash(NationalityController.DEFAULT_PATH)
                .slash(model.getId())
                .withSelfRel();
        model.add(selfLink);
        return model;
    }

}
