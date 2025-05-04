package pl.derleta.nebula.domain.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.domain.builder.impl.GenderBuilderImpl;
import pl.derleta.nebula.domain.entity.GenderEntity;
import pl.derleta.nebula.domain.model.Gender;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between GenderEntity and Gender objects.
 * This class provides static methods to transform data from database entities
 * (GenderEntity) into business models (Gender).
 * <p>
 * The class is designed to be non-instantiable and serves as an abstraction layer
 * for mapping operations.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenderMapper {

    /**
     * Converts a list of GenderEntity objects to a list of Gender objects.
     * This method maps each GenderEntity in the provided list to a corresponding Gender object.
     *
     * @param entities the list of GenderEntity objects to be converted
     * @return a list of Gender objects constructed from the provided list of GenderEntity objects
     */
    public static List<Gender> toGenders(final List<GenderEntity> entities) {
        return entities.stream().map(GenderMapper::toGender).collect(Collectors.toList());
    }

    /**
     * Converts a GenderEntity object to a Gender object.
     * This method maps the fields of a GenderEntity instance to a Gender record using a builder implementation.
     *
     * @param entity the GenderEntity object to be transformed
     * @return a Gender object constructed from the provided GenderEntity
     */
    public static Gender toGender(final GenderEntity entity) {
        return new GenderBuilderImpl()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

}
