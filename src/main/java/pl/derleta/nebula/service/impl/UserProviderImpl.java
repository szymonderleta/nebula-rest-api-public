package pl.derleta.nebula.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.derleta.nebula.domain.entity.UserEntity;
import pl.derleta.nebula.domain.mapper.NebulaUserMapper;
import pl.derleta.nebula.domain.model.NebulaUser;
import pl.derleta.nebula.repository.UserRepository;
import pl.derleta.nebula.service.UserProvider;

import java.util.Optional;

/**
 * Implementation of the {@link UserProvider} interface that provides functionality
 * for retrieving user data from the database.
 * <p>
 * This class uses a {@link UserRepository} to access persisted user entities
 * and maps them to {@link NebulaUser} objects using the {@link NebulaUserMapper}.
 * <p>
 * It is marked as a Spring {@code @Service} component and handles transactions
 * for its database operations.
 */
@AllArgsConstructor
@Service
public class UserProviderImpl implements UserProvider {

    private final UserRepository repository;

    /**
     * Retrieves a NebulaUser object by its unique user ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the NebulaUser object corresponding to the given user ID,
     * or null if no user with the specified ID exists
     */
    @Override
    @Transactional
    public NebulaUser get(Long userId) {
        Optional<UserEntity> optional = repository.findById(userId);
        return optional.map(NebulaUserMapper::toUser).orElse(null);
    }

}
