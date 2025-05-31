package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.*;

public interface UserAchievementBuilder {

    UserAchievement build();

    UserAchievementBuilder userId(long userId);

    UserAchievementBuilder achievementId(int achievementId);

    UserAchievementBuilder value(int value);

    UserAchievementBuilder level(int level);

    UserAchievementBuilder progress(int progress);

    UserAchievementBuilder achievement(Achievement achievement);

}
