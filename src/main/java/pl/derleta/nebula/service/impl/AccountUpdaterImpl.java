package pl.derleta.nebula.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.derleta.nebula.controller.mapper.AuthServRegistrationRequestMapper;
import pl.derleta.nebula.controller.request.*;
import pl.derleta.nebula.controller.response.AccountResponse;
import pl.derleta.nebula.controller.response.JwtTokenResponse;
import pl.derleta.nebula.domain.entity.*;
import pl.derleta.nebula.domain.rest.UserRoles;
import pl.derleta.nebula.domain.types.AccountResponseType;
import pl.derleta.nebula.repository.*;
import pl.derleta.nebula.service.AccountUpdater;
import pl.derleta.nebula.util.HttpAuthClient;
import pl.derleta.nebula.util.IdUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the AccountUpdater interface.
 * Provides methods for managing user accounts, including registration, confirmation,
 * unlocking accounts, resetting passwords, updating passwords, and generating JWT tokens.
 * Integrates with external authentication services and the Nebula platform for account management.
 */
@Service
public class AccountUpdaterImpl implements AccountUpdater {

    final HttpAuthClient httpAuthServClient;

    final UserSettingsRepository settingsRepository;
    final UsersGamesRepository gamesRepository;
    final UserRepository userRepository;
    final NationalityRepository nationalityRepository;
    final GenderRepository genderRepository;
    final GameRepository gameRepository;
    final AchievementRepository achievementRepository;

    /**
     * Constructor for AccountUpdaterImpl.
     *
     * @param httpAuthServClient    The HTTP authentication service client used for external service authentication.
     * @param settingsRepository    The repository for managing user settings.
     * @param gamesRepository       The repository for managing user games.
     * @param userRepository        The repository for managing user data.
     * @param nationalityRepository The repository for managing nationalities.
     * @param genderRepository      The repository for managing genders.
     * @param gameRepository        The repository for managing game information.
     * @param achievementRepository The repository for managing achievements.
     */
    @Autowired
    public AccountUpdaterImpl(HttpAuthClient httpAuthServClient, UserSettingsRepository settingsRepository, UsersGamesRepository gamesRepository, UserRepository userRepository, NationalityRepository nationalityRepository, GenderRepository genderRepository, GameRepository gameRepository, AchievementRepository achievementRepository) {
        this.httpAuthServClient = httpAuthServClient;
        this.settingsRepository = settingsRepository;
        this.gamesRepository = gamesRepository;
        this.userRepository = userRepository;
        this.nationalityRepository = nationalityRepository;
        this.genderRepository = genderRepository;
        this.gameRepository = gameRepository;
        this.achievementRepository = achievementRepository;
    }

    /**
     * Handles the registration of a new account by processing the provided request.
     * This method validates the request, performs registration on the authentication service,
     * and optionally on an additional service, ensuring a consistent response is returned.
     *
     * @param request the incoming request containing the necessary information for account registration
     * @return an AccountResponse indicating the success or failure of the registration
     * process and the type of response provided
     */
    @Override
    @Transactional
    public AccountResponse register(Request request) {
        if (request instanceof AccountRegistrationRequest instance) {
            AuthServRegistrationRequest authServRequest = AuthServRegistrationRequestMapper.getAccountAuthRegistration(instance);
            AccountResponse authServResponse = httpAuthServClient.registerUser(authServRequest);
            if (authServResponse.isSuccess()) {
                AccountResponse nebulaResponse = registerOnNebula(instance);
                if (nebulaResponse.isSuccess()) return authServResponse;
                else return nebulaResponse;
            } else
                return new AccountResponse(false, AccountResponseType.NEBULA_BAD_REGISTRATION_RESPONSE_INSTANCE);
        }
        return new AccountResponse(false, AccountResponseType.NEBULA_BAD_REGISTRATION_REQUEST_INSTANCE);
    }

    /**
     * Confirms a user account based on the provided confirmation request.
     *
     * @param confirmation the user confirmation request containing necessary details for account confirmation
     * @return an AccountResponse object representing the result of the confirmation process
     */
    @Override
    public AccountResponse confirm(UserConfirmationRequest confirmation) {
        return httpAuthServClient.confirmAccount(confirmation);
    }

    /**
     * Unlocks an account associated with the given identifier.
     *
     * @param id the unique identifier of the account to be unlocked
     * @return an AccountResponse object containing details of the unlocked account
     */
    @Override
    public AccountResponse unlock(Long id) {
        return httpAuthServClient.unlockAccount(id);
    }

    /**
     * Resets the password for the account associated with the given email.
     *
     * @param email the email address of the account for which the password is to be reset
     * @return an AccountResponse containing the response details of the password reset operation
     */
    @Override
    public AccountResponse resetPassword(String email) {
        return httpAuthServClient.resetPassword(email);
    }

    /**
     * Generates a JWT token based on the provided authentication request.
     *
     * @param authRequest the authentication request containing the credentials or data required for token generation
     * @return a JwtTokenResponse containing the generated JWT token and related information
     */
    @Override
    public JwtTokenResponse generateToken(AuthEmailRequest authRequest) {
        return httpAuthServClient.generateToken(authRequest);
    }

    /**
     * Updates the password for an account using the provided JWT token and password update request.
     *
     * @param jwtToken       the JSON Web Token used to authenticate the request
     * @param passwordUpdate the request object containing the existing and new password details
     * @return an AccountResponse object containing details of the account after the password update
     */
    @Override
    public AccountResponse updatePassword(String jwtToken, PasswordUpdateRequest passwordUpdate) {
        return httpAuthServClient.updatePassword(jwtToken, passwordUpdate);
    }

    /**
     * Registers a user on the Nebula platform by creating the user and assigning roles.
     *
     * @param registrationRequest the request object containing the user registration information
     * @return an AccountResponse indicating the success or failure of the registration process
     */
    private AccountResponse registerOnNebula(AccountRegistrationRequest registrationRequest) {
        UserRoles userRoles = httpAuthServClient.getAccount(registrationRequest);
        if (!createUser(userRoles, registrationRequest))
            return new AccountResponse(false, AccountResponseType.USER_NOT_CREATED_IN_NEBULA_DB);
        return new AccountResponse(true, AccountResponseType.USER_CREATED_IN_NEBULA_DB);
    }

    /**
     * Creates a user entity based on the provided user roles and registration request,
     * and saves the entity to the repository.
     *
     * @param userRoles           The roles and associated user details required for creating the user.
     * @param registrationRequest The registration details including birthdate, nationality, and gender.
     * @return true if the user's ID matches the saved entity's ID; false otherwise.
     */
    private boolean createUser(UserRoles userRoles, AccountRegistrationRequest registrationRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userRoles.getUser().getUserId());
        userEntity.setEmail(userRoles.getUser().getEmail());
        userEntity.setLogin(userRoles.getUser().getUsername());
        userEntity.setBirthDate(registrationRequest.getBirthdate());
        userEntity.updateAge();
        userEntity.setNationality(
                nationalityRepository.findById(registrationRequest.getNationality())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "No Nationality found for ID: " + registrationRequest.getNationality()))
        );
        userEntity.setGender(
                genderRepository.findById(registrationRequest.getGender())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "No Gender found for ID: " + registrationRequest.getGender()))
        );
        userEntity.setSettings(
                createUserSettingsEntity(userRoles.getUser().getUserId())
        );
        userEntity.setGames(
                getGames()
        );
        userEntity.setAchievements(
                getAchievements(userEntity.getId(), userEntity)
        );
        UserEntity result = userRepository.save(userEntity);
        return result.getId() == userRoles.getUser().getUserId();
    }

    /**
     * Retrieves a list of all games from the repository.
     *
     * @return a list of GameEntity objects representing all the games in the repository
     */
    private List<GameEntity> getGames() {
        return gameRepository.findAll();
    }

    /**
     * Retrieves a list of user achievement entities for the specified user.
     *
     * @param userId     the unique identifier of the user
     * @param userEntity the user entity associated with the user
     * @return a list of UserAchievementEntity objects representing the user's achievements
     */
    private List<UserAchievementEntity> getAchievements(final long userId, final UserEntity userEntity) {
        List<AchievementEntity> achievements = achievementRepository.findAll();
        return achievements.stream()
                .map(achievement -> createUserAchievementEntity(userId, userEntity, achievement))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new UserAchievementEntity with the provided user ID, user entity, and achievement entity.
     * Initializes the achievement values to default values.
     *
     * @param userId      The ID of the user associated with the achievement.
     * @param userEntity  The user entity to be linked with the achievement.
     * @param achievement The achievement entity to be associated with the user.
     * @return A new instance of UserAchievementEntity with the specified parameters and default values.
     */
    private UserAchievementEntity createUserAchievementEntity(final long userId, final UserEntity userEntity,
                                                              final AchievementEntity achievement) {
        final int DEFAULT_ACHIEVEMENT_VALUES = 0;
        return new UserAchievementEntity(
                IdUtil.getUserAchievementId(userId, achievement.getId()),
                userEntity,
                achievement,
                DEFAULT_ACHIEVEMENT_VALUES,
                DEFAULT_ACHIEVEMENT_VALUES,
                DEFAULT_ACHIEVEMENT_VALUES
        );
    }

    /**
     * Creates a UserSettingsEntity for a specified user ID.
     *
     * @param userId the ID of the user for whom the UserSettingsEntity is being created
     * @return a UserSettingsEntity containing general and sound settings for the specified user
     */
    private UserSettingsEntity createUserSettingsEntity(long userId) {
        UserSettingsGeneralEntity userSettingsGeneralEntity = createUserSettingsGeneralEntity(userId);
        UserSettingsSoundEntity userSettingsSoundEntity = createUserSettingsSoundEntity(userId);
        return new UserSettingsEntity(userId, userSettingsGeneralEntity, userSettingsSoundEntity);
    }

    /**
     * Creates and returns a new UserSettingsGeneralEntity object with a default theme entity
     * and the specified user ID.
     *
     * @param userId the unique identifier of the user for whom the settings entity is created
     * @return a UserSettingsGeneralEntity containing the user ID and a default theme entity
     */
    private UserSettingsGeneralEntity createUserSettingsGeneralEntity(long userId) {
        ThemeEntity themeEntity = createDefaultThemeEntity();
        return new UserSettingsGeneralEntity(userId, themeEntity);
    }

    /**
     * Creates and returns a default ThemeEntity instance with pre-defined values.
     *
     * @return a ThemeEntity object initialized with default values.
     */
    private ThemeEntity createDefaultThemeEntity() {
        return new ThemeEntity(17, "Default");
    }

    /**
     * Creates and initializes a new UserSettingsSoundEntity object with default sound settings.
     *
     * @param userId the unique identifier of the user for whom the sound settings entity is being created
     * @return a newly created UserSettingsSoundEntity initialized with default settings
     */
    private UserSettingsSoundEntity createUserSettingsSoundEntity(long userId) {
        return new UserSettingsSoundEntity(userId, false, true, 100, 100, 100, 100);
    }

}
