package pl.derleta.nebula.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.controller.response.ThemeResponse;
import pl.derleta.nebula.domain.model.Theme;

/**
 * A utility class for mapping Theme objects to corresponding ThemeResponse DTOs.
 * This class is implemented as a static utility and is not intended to be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThemeApiMapper {

    private final static String URL_GROUP_PATH = "http://milkyway.local/nebula/res/icon/theme/";

    /**
     * Converts a Theme object into a ThemeResponse object.
     *
     * @param item the Theme object to be converted, containing theme-specific details such as id and name
     * @return a ThemeResponse object containing the transformed data from the given Theme object,
     * including id, name, and an image URL constructed using the theme's id
     */
    public static ThemeResponse toResponse(final Theme item) {
        return ThemeResponse.builder()
                .id(item.id())
                .name(item.name())
                .imgURL(URL_GROUP_PATH + item.id() + ".png")
                .build();
    }

}
