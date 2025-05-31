package pl.derleta.nebula.controller.assembler;

import jakarta.annotation.Nonnull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import pl.derleta.nebula.controller.UserController;
import pl.derleta.nebula.controller.mapper.NebulaUserApiMapper;
import pl.derleta.nebula.controller.response.NebulaUserResponse;
import pl.derleta.nebula.domain.model.NebulaUser;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * UserModelAssembler is responsible for converting NebulaUser domain entities
 * into their corresponding NebulaUserResponse resource representations, adhering
 * to HATEOAS principles by including necessary hypermedia links.
 * <p>
 * This class extends RepresentationModelAssemblerSupport, associating the
 * NebulaUser domain model with the NebulaUserResponse resource representation.
 * It provides a standardized way to transform domain entities into resource
 * representations for API responses while integrating hypermedia links seamlessly.
 * <p>
 * Responsibilities:
 * - Converts NebulaUser entities to NebulaUserResponse models.
 * - Attaches a self-referential link to the resulting NebulaUserResponse, allowing
 * clients to navigate to the specific resource endpoint representing the entity.
 * <p>
 * Constructor:
 * The assembler initializes its configuration by linking the UserController
 * class with the NebulaUserResponse class to facilitate the mapping between
 * the domain model and resource representation.
 * <p>
 * Core Functionality:
 * - {@code toModel(NebulaUser entity)}:
 * - Converts a NebulaUser entity to a NebulaUserResponse object.
 * - Enhances the model representation with a self-referential hyperlink.
 * <p>
 * This class is annotated with @Component, making it a Spring-managed bean,
 * which can be injected into other components or controllers, enabling its usage
 * throughout the application for consistent model transformation.
 */
@Component
public final class UserModelAssembler extends RepresentationModelAssemblerSupport<NebulaUser, NebulaUserResponse> {

    public UserModelAssembler() {
        super(UserController.class, NebulaUserResponse.class);
    }

    /**
     * Converts a NebulaUser domain entity into a NebulaUserResponse resource representation.
     * The resulting resource is enhanced with a self-referential hyperlink for HATEOAS compliance.
     *
     * @param entity the NebulaUser domain object to be converted into a resource representation
     * @return a NebulaUserResponse object containing the converted data and associated hypermedia links
     */
    @Override
    @Nonnull
    public NebulaUserResponse toModel(@Nonnull NebulaUser entity) {
        NebulaUserResponse model = NebulaUserApiMapper.toResponse(entity);
        Link selfLink = linkTo(UserController.class)
                .slash(UserController.DEFAULT_PATH)
                .slash(model.getId())
                .withSelfRel();
        model.add(selfLink);
        return model;
    }

}
