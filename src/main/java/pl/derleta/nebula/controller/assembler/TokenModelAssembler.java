package pl.derleta.nebula.controller.assembler;

import jakarta.annotation.Nonnull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import pl.derleta.nebula.controller.TokenController;
import pl.derleta.nebula.controller.mapper.TokenDataApiMapper;
import pl.derleta.nebula.controller.response.TokenDataResponse;
import pl.derleta.nebula.domain.token.TokenData;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * TokenModelAssembler is responsible for converting TokenData domain entities
 * into their corresponding TokenDataResponse resource representations, utilizing
 * the HATEOAS principles by appending hypermedia links.
 * <p>
 * This class extends RepresentationModelAssemblerSupport, associating the
 * TokenData domain model with the TokenDataResponse resource representation.
 * It ensures a consistent transformation between the internal data model and
 * the external API response format with integrated hypermedia support.
 * <p>
 * Responsibilities:
 * - Converts TokenData entities to TokenDataResponse models.
 * - Attaches a self-referential link to the resulting TokenDataResponse,
 * enabling API consumers to navigate to the corresponding resource endpoint.
 * <p>
 * Constructor:
 * The assembler is initialized by associating the TokenController with the
 * TokenDataResponse class for mapping domain models to their respective
 * resource representations.
 * <p>
 * Core Implementation:
 * - The {@code toModel(TokenData entity)} method:
 * - Transforms a TokenData entity into a TokenDataResponse object.
 * - Enhances the resource representation with a self-referential link
 * pointing to the API endpoint associated with the resource.
 * <p>
 * This class is registered as a Spring component, making it available for
 * dependency injection in other components and controllers across the application.
 */
@Component
public final class TokenModelAssembler extends RepresentationModelAssemblerSupport<TokenData, TokenDataResponse> {

    public TokenModelAssembler() {
        super(TokenController.class, TokenDataResponse.class);
    }

    /**
     * Converts a TokenData domain entity into a TokenDataResponse resource representation.
     * The resulting resource includes a self-referential hyperlink for HATEOAS compliance.
     *
     * @param entity the TokenData domain object to be converted into a resource representation
     * @return a TokenDataResponse object containing the converted data and associated hypermedia links
     */
    @Override
    @Nonnull
    public TokenDataResponse toModel(@Nonnull TokenData entity) {
        TokenDataResponse model = TokenDataApiMapper.toResponse(entity);
        Link selfLink = linkTo(TokenController.class)
                .slash(TokenController.DEFAULT_PATH)
                .withSelfRel();
        model.add(selfLink);
        return model;
    }

}
