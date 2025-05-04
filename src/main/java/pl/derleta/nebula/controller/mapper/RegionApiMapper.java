package pl.derleta.nebula.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.controller.response.RegionResponse;
import pl.derleta.nebula.domain.model.Region;

/**
 * A utility class designed for mapping Region domain objects to corresponding RegionResponse DTOs.
 * This class is implemented as a static utility and is not intended to be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegionApiMapper {

    /**
     * Converts a Region object into a RegionResponse object.
     *
     * @param item the Region object to be converted, containing the region's id and name
     * @return a RegionResponse object containing the transformed data from the given Region object,
     * including the id and name
     */
    public static RegionResponse toResponse(final Region item) {
        return RegionResponse.builder()
                .id(item.id())
                .name(item.name())
                .build();
    }

}
