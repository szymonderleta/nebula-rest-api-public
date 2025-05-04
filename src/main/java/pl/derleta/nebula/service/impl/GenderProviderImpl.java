package pl.derleta.nebula.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.derleta.nebula.domain.mapper.GenderMapper;
import pl.derleta.nebula.domain.model.Gender;
import pl.derleta.nebula.exceptions.GenderNotFoundException;
import pl.derleta.nebula.repository.GenderRepository;
import pl.derleta.nebula.service.GenderProvider;

import java.util.List;

/**
 * Implementation of the GenderProvider interface that provides methods
 * to retrieve individual gender information or all available gender records.
 * This service interacts with the GenderRepository for data access and
 * utilizes GenderMapper to convert between entities and model objects.
 */
@AllArgsConstructor
@Service
public class GenderProviderImpl implements GenderProvider {

    private final GenderRepository repository;

    /**
     * Retrieves a Gender object corresponding to the given ID.
     * This method fetches the gender entity from the repository,
     * converts it to a domain model using the GenderMapper, and
     * returns it. Throws exceptions for invalid or non-existent IDs.
     *
     * @param id the unique identifier of the gender to be retrieved
     * @return the Gender object associated with the specified ID
     * @throws IllegalArgumentException if the provided ID is null
     * @throws GenderNotFoundException  if a gender with the specified ID is not found
     */
    @Override
    public Gender get(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Gender ID cannot be null");
        }
        var entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new GenderNotFoundException("Gender with id: " + id + " not found");
        }
        return GenderMapper.toGender(entity.get());
    }

    /**
     * Retrieves all gender entities from the repository and maps them to a list of Gender objects.
     *
     * @return a list of Gender objects representing all gender entries in the repository
     */
    @Override
    public List<Gender> getAll() {
        var entities = repository.findAll();
        return GenderMapper.toGenders(entities);
    }

}
