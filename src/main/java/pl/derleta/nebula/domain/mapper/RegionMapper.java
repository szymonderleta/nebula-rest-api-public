package pl.derleta.nebula.domain.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.domain.builder.impl.RegionBuilderImpl;
import pl.derleta.nebula.domain.entity.RegionEntity;
import pl.derleta.nebula.domain.model.Region;

/**
 * Utility class for mapping between RegionEntity and Region objects.
 * This class provides static methods to convert database entities (RegionEntity)
 * into business models (Region).
 * <p>
 * The class is designed to be non-instantiable and provides only static methods
 * for mapping operations.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegionMapper {

    /**
     * Converts a RegionEntity object to a Region object.
     * This method maps the fields of the RegionEntity instance to the corresponding attributes
     * of a Region record using a builder implementation.
     *
     * @param entity the RegionEntity object to be converted
     * @return a Region object constructed from the provided RegionEntity object
     */
    public static Region toRegion(final RegionEntity entity) {
        return new RegionBuilderImpl()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

}
