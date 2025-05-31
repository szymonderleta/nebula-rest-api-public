package pl.derleta.nebula.domain.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.domain.builder.impl.NebulaUserBuilderImpl;
import pl.derleta.nebula.domain.entity.UserEntity;
import pl.derleta.nebula.domain.model.NebulaUser;

/**
 * Utility class for mapping UserEntity objects to NebulaUser objects.
 * This class is designed as a final utility class and cannot be instantiated.
 * It provides a single static method to facilitate the transformation
 * of user-related data between the application and database layers.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NebulaUserMapper {

    /**
     * Converts a UserEntity object to a NebulaUser object.
     *
     * @param entity the UserEntity object to be converted
     * @return a NebulaUser object constructed from the provided UserEntity
     */
    public static NebulaUser toUser(final UserEntity entity) {
        return new NebulaUserBuilderImpl()
                .id(entity.getId())
                .login(entity.getLogin())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .age(entity.getAge() != null ? entity.getAge() : 0)
                .birthDate(entity.getBirthDate())
                .gender(GenderMapper.toGender(entity.getGender()))
                .nationality(NationalityMapper.toNationality(entity.getNationality()))
                .settings(UserSettingsMapper.toSetting(entity.getSettings()))
                .games(GameMapper.toGames(entity.getGames()))
                .achievements(UserAchievementMapper.toUserAchievements(entity.getAchievements()))
                .build();
    }

}
