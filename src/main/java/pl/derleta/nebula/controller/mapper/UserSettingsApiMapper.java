package pl.derleta.nebula.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.controller.response.UserSettingsResponse;
import pl.derleta.nebula.domain.model.UserSettings;

/**
 * A utility class for mapping UserSettings domain objects to corresponding UserSettingsResponse DTOs.
 * This class is designed to be used as a static utility and is not intended to be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserSettingsApiMapper {

    /**
     * Converts a UserSettings object into a UserSettingsResponse object.
     *
     * @param item the UserSettings object to be converted
     * @return a UserSettingsResponse object containing the data from the given UserSettings object
     */
    public static UserSettingsResponse toResponse(final UserSettings item) {
        return UserSettingsResponse.builder()
                .userId(item.userId())
                .general(item.general())
                .sound(item.sound())
                .build();
    }

}
