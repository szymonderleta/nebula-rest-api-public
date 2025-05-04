package pl.derleta.nebula.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.controller.response.NationalityResponse;
import pl.derleta.nebula.domain.model.Nationality;

/**
 * A utility class designed for mapping Nationality domain objects to corresponding NationalityResponse DTOs.
 * This class is implemented as a static utility and cannot be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NationalityApiMapper {

    private final static String URL_GROUP_PATH = "http://milkyway.local/nebula/res/icon/nationality/";

    /**
     * Converts a Nationality object into a NationalityResponse object.
     *
     * @param item the Nationality object to be converted, containing details such as id, name,
     *             code, region, and other related data
     * @return a NationalityResponse object containing the transformed data from the given Nationality object,
     * including id, name, code, region response, and an image URL constructed using the nationality's id
     */
    public static NationalityResponse toResponse(final Nationality item) {
        return NationalityResponse.builder()
                .id(item.id())
                .name(item.name())
                .code(item.code())
                .region(RegionApiMapper.toResponse(item.region()))
                .imgURL(URL_GROUP_PATH + item.id() + ".png")
                .build();
    }

}
