package pl.derleta.nebula.domain.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.domain.builder.impl.NationalityBuilderImpl;
import pl.derleta.nebula.domain.entity.NationalityEntity;
import pl.derleta.nebula.domain.model.Nationality;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between NationalityEntity and Nationality objects.
 * This class provides methods to convert database entities (NationalityEntity) to business
 * models (Nationality).
 * <p>
 * The class is designed to be non-instantiable and provides only static methods
 * for mapping operations.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NationalityMapper {

    /**
     * Converts a list of NationalityEntity objects to a list of Nationality objects.
     * This method maps each NationalityEntity in the provided list to a corresponding Nationality object.
     *
     * @param entities the list of NationalityEntity objects to be converted
     * @return a list of Nationality objects constructed from the provided list of NationalityEntity objects
     */
    public static List<Nationality> toNationalities(final List<NationalityEntity> entities) {
        return entities.stream().map(NationalityMapper::toNationality).collect(Collectors.toList());
    }

    /**
     * Converts a NationalityEntity object to a Nationality object.
     * This method maps the fields of the NationalityEntity instance to a Nationality record
     * using a builder implementation and a region mapping utility.
     *
     * @param entity the NationalityEntity object to be converted
     * @return a Nationality object constructed from the provided NationalityEntity object
     */
    public static Nationality toNationality(final NationalityEntity entity) {
        return new NationalityBuilderImpl()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .region(RegionMapper.toRegion(entity.getRegion()))
                .build();
    }

}
