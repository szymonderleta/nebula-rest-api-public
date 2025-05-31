package pl.derleta.nebula.service;

import org.springframework.data.domain.Page;
import pl.derleta.nebula.domain.model.UserAchievement;
import pl.derleta.nebula.controller.request.UserAchievementFilterRequest;

import java.util.List;

public interface UserAchievementProvider {

    UserAchievement get(Long userId, Integer achievementId);

    List<UserAchievement> getList(Long userId);

    Page<UserAchievement> getPage(UserAchievementFilterRequest request);

}
