package pl.derleta.nebula.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.controller.response.NebulaUserResponse;
import pl.derleta.nebula.domain.mapper.NebulaUserAchievementMapper;
import pl.derleta.nebula.domain.model.NebulaUser;

/**
 * A utility class for mapping a {@link NebulaUser} object to a {@link NebulaUserResponse} object.
 * This class provides a single static method to convert the domain model representation of a user
 * into its corresponding API response representation.
 * <p>
 * The class is not meant to be instantiated and hence has a private constructor.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NebulaUserApiMapper {

    /**
     * Converts a {@link NebulaUser} object to a {@link NebulaUserResponse} object by mapping
     * each property from the provided NebulaUser to the corresponding property in NebulaUserResponse.
     *
     * @param item the {@link NebulaUser} object to be converted to a {@link NebulaUserResponse}
     * @return a {@link NebulaUserResponse} object containing the data mapped from the provided {@link NebulaUser}
     */
    public static NebulaUserResponse toResponse(final NebulaUser item) {
        return NebulaUserResponse.builder()
                .id(item.id())
                .login(item.login())
                .email(item.email())
                .firstName(item.firstName())
                .lastName(item.lastName())
                .age(item.age())
                .birthDate(item.birthDate())
                .gender(item.gender())
                .nationality(item.nationality())
                .settings(item.settings())
                .games(item.games())
                .achievements(NebulaUserAchievementMapper.toList(item.achievements()))
                .build();
    }

}
