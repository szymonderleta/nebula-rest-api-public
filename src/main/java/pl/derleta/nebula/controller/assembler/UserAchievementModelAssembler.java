package pl.derleta.nebula.controller.assembler;

import jakarta.annotation.Nonnull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import pl.derleta.nebula.controller.UserAchievementController;
import pl.derleta.nebula.controller.response.UserAchievementResponse;
import pl.derleta.nebula.domain.model.UserAchievement;
import pl.derleta.nebula.controller.mapper.UserAchievementApiMapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * UserAchievementModelAssembler is responsible for converting UserAchievement domain
 * entities into their corresponding UserAchievementResponse resource representations,
 * incorporating HATEOAS principles by including relevant hypermedia links.
 * <p>
 * This class extends RepresentationModelAssemblerSupport, associating the UserAchievement
 * domain model with the UserAchievementResponse resource representation. It serves as
 * an intermediary to ensure consistent entity-to-resource conversion and integration
 * with Spring's HATEOAS library.
 * <p>
 * Functionality:
 * - Transforms UserAchievement entities into UserAchievementResponse models.
 * - Adds a self-referential link to the resulting UserAchievementResponse, enabling
 * API consumers to directly access the resource endpoint.
 * <p>
 * Constructor:
 * Initializes the assembler by associating the UserAchievementController with
 * UserAchievementResponse, mapping domain entities to their resource representations.
 * <p>
 * Key Method:
 * - {@code toModel(UserAchievement entity)}: Converts a UserAchievement entity into
 * a UserAchievementResponse, attaching a self-referential link reflecting the resource location.
 * <p>
 * The assembler is declared as a Spring component, making it available for dependency
 * injection and reuse across the application where necessary.
 */
@Component
public final class UserAchievementModelAssembler extends RepresentationModelAssemblerSupport<UserAchievement, UserAchievementResponse> {

    public UserAchievementModelAssembler() {
        super(UserAchievementController.class, UserAchievementResponse.class);
    }

    /**
     * Converts a {@link UserAchievement} entity into a {@link UserAchievementResponse} model representation.
     * Adds a self-referential HATEOAS link to the resulting {@code UserAchievementResponse}.
     *
     * @param entity the {@link UserAchievement} entity to be converted; must not be null
     * @return the {@link UserAchievementResponse} model representation with an added self-link
     */
    @Override
    @Nonnull
    public UserAchievementResponse toModel(@Nonnull UserAchievement entity) {
        UserAchievementResponse model = UserAchievementApiMapper.toResponse(entity);
        Link selfLink = linkTo(UserAchievementController.class)
                .slash(UserAchievementController.DEFAULT_PATH)
                .slash(model.getUserId())
                .slash(model.getAchievementId())
                .withSelfRel();
        model.add(selfLink);
        return model;
    }

}
