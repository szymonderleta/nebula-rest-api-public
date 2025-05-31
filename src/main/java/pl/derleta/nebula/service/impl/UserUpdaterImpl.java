package pl.derleta.nebula.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.derleta.nebula.controller.request.ProfileUpdateRequest;
import pl.derleta.nebula.controller.request.Request;
import pl.derleta.nebula.domain.entity.UserSettingsEntity;
import pl.derleta.nebula.domain.mapper.NebulaUserMapper;
import pl.derleta.nebula.domain.mapper.UserSettingsMapper;
import pl.derleta.nebula.domain.model.NebulaUser;
import pl.derleta.nebula.domain.model.UserSettings;
import pl.derleta.nebula.repository.UserRepository;
import pl.derleta.nebula.repository.UserSettingsRepository;
import pl.derleta.nebula.service.UserUpdater;

/**
 * Implementation of the UserUpdater interface that provides functionality to update user profiles
 * and user settings. This service relies on UserRepository and UserSettingsRepository for data operations.
 */
@AllArgsConstructor
@Service
public class UserUpdaterImpl implements UserUpdater {

    private final UserRepository repository;
    private final UserSettingsRepository userSettingsRepository;

    /**
     * Updates the profile information of a user by processing the provided request.
     * If the request is an instance of ProfileUpdateRequest, the user's details
     * are updated in the repository and the updated user's information is returned.
     *
     * @param request the request object containing the user profile information to be updated
     * @return the updated NebulaUser object if the request is valid, otherwise null
     */
    @Transactional
    @Override
    public NebulaUser updateProfile(Request request) {
        if (request instanceof ProfileUpdateRequest instance) {
            repository.updateUserDetails(
                    instance.getUserId(), instance.getFirstName(), instance.getLastName(),
                    instance.getBirthdate(), instance.getNationalityId(), instance.getGenderId());
            var result = repository.getReferenceById(instance.getUserId());
            return NebulaUserMapper.toUser(result);
        }
        return null;
    }

    /**
     * Updates the user settings in the system. Validates user IDs in the settings
     * object before persisting the changes in the repository. Additionally, updates
     * the user's last modified timestamp.
     *
     * @param userSettings the user settings object containing updated configuration details
     * @return the updated user settings object after persisting changes in the repository
     * @throws IllegalArgumentException if the user IDs in the user settings do not match
     */
    @Transactional
    @Override
    public UserSettings updateSettings(UserSettings userSettings) {
        validateUserIdsInSettings(userSettings);
        UserSettingsEntity entity = UserSettingsMapper.toEntity(userSettings);
        var result = userSettingsRepository.save(entity);
        repository.updateUserUpdatedAt(userSettings.userId());
        return UserSettingsMapper.toSetting(result);
    }

    /**
     * Validates that all user IDs present in the user settings object match.
     * This includes the main user ID, general settings user ID, and sound settings user ID.
     * If there is a mismatch, an IllegalArgumentException is thrown.
     *
     * @param userSettings the user settings object containing user IDs to validate
     * @throws IllegalArgumentException if the user IDs in the user settings do not match
     */
    private void validateUserIdsInSettings(UserSettings userSettings) {
        Long userId = userSettings.userId();
        Long generalUserId = userSettings.general().userId();
        Long soundUserId = userSettings.sound().userId();

        if (!userId.equals(generalUserId) || !userId.equals(soundUserId)) {
            throw new IllegalArgumentException("User IDs in settings do not match!");
        }
    }

}
