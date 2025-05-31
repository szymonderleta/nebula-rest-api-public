package pl.derleta.nebula.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.controller.response.GenderResponse;
import pl.derleta.nebula.domain.model.Gender;

/**
 * A utility class designed for mapping Gender domain objects to corresponding GenderResponse DTOs.
 * This class is implemented as a static utility and cannot be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenderApiMapper {

    private final static String URL_GROUP_PATH = "http://milkyway.local/nebula/res/icon/gender/";

    /**
     * Converts a Gender object into a GenderResponse object.
     *
     * @param item the Gender object to be converted, containing the id and name of the gender
     * @return a GenderResponse object containing the transformed data from the given Gender object,
     * including id, name, and an image URL constructed using the gender's id
     */
    public static GenderResponse toResponse(final Gender item) {
        return GenderResponse.builder()
                .id(item.id())
                .name(item.name())
                .imgURL(URL_GROUP_PATH + item.id() + ".png")
                .build();
    }

}
