package pl.derleta.nebula.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.derleta.nebula.domain.mapper.ThemeMapper;
import pl.derleta.nebula.domain.model.Theme;
import pl.derleta.nebula.exceptions.ThemeNotFoundException;
import pl.derleta.nebula.repository.ThemeRepository;
import pl.derleta.nebula.service.ThemeProvider;

import java.util.List;

/**
 * An implementation of the ThemeProvider interface that provides functionality
 * for accessing Theme data. This service retrieves theme data from the repository
 * and uses ThemeMapper to map the data to the appropriate domain model.
 */
@AllArgsConstructor
@Service
public class ThemeProviderImpl implements ThemeProvider {

    private final ThemeRepository repository;

    /**
     * Retrieves a Theme object by its unique identifier.
     *
     * @param id the unique identifier of the Theme to be retrieved; must not be null
     * @return the Theme object corresponding to the given id
     * @throws IllegalArgumentException if the provided id is null
     * @throws ThemeNotFoundException   if no Theme is found with the specified id
     */
    @Override
    public Theme get(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Theme ID cannot be null");
        }
        var entity = repository.findById(id);
        if (entity.isEmpty())
            throw new ThemeNotFoundException("Theme with id: " + id + " not found");
        return ThemeMapper.toTheme(entity.get());
    }

    /**
     * Retrieves all theme entities from the repository and maps them to a list of `Theme` objects.
     *
     * @return a list of all available themes
     */
    @Override
    public List<Theme> getAll() {
        var entities = repository.findAll();
        return ThemeMapper.toThemes(entities);
    }

}
