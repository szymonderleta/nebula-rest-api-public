package pl.derleta.nebula.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.derleta.nebula.domain.mapper.NationalityMapper;
import pl.derleta.nebula.domain.model.Nationality;
import pl.derleta.nebula.exceptions.NationalityNotFoundException;
import pl.derleta.nebula.repository.NationalityRepository;
import pl.derleta.nebula.service.NationalityProvider;

import java.util.List;

/**
 * Implementation of the NationalityProvider interface that provides methods
 * for retrieving nationality data. This service relies on NationalityRepository
 * for database operations and NationalityMapper for data transformation.
 */
@AllArgsConstructor
@Service
public class NationalityProviderImpl implements NationalityProvider {

    private final NationalityRepository repository;

    /**
     * Retrieves a Nationality object by its unique identifier.
     *
     * @param id the unique identifier of the nationality to retrieve; must not be null
     * @return the Nationality object corresponding to the specified id
     * @throws IllegalArgumentException     if the provided id is null
     * @throws NationalityNotFoundException if no nationality is found for the given id
     */
    @Override
    public Nationality get(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Nationality ID cannot be null");
        }
        var entity = repository.findById(id);
        if (entity.isEmpty()) {
            throw new NationalityNotFoundException("Nationality with id: " + id + " not found");
        }
        return NationalityMapper.toNationality(entity.get());

    }

    /**
     * Retrieves all nationalities from the underlying repository and maps them to a list of domain models.
     *
     * @return a list of all nationalities as domain model objects
     */
    @Override
    public List<Nationality> getAll() {
        var entities = repository.findAll();
        return NationalityMapper.toNationalities(entities);
    }

}
