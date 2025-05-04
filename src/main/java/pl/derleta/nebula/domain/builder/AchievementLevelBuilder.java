package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.AchievementLevel;

public interface AchievementLevelBuilder {

    AchievementLevel build();

    AchievementLevelBuilder level(int level);

    AchievementLevelBuilder value(int value);

}
