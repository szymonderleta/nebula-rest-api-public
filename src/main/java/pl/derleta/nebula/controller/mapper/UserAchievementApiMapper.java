package pl.derleta.nebula.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import pl.derleta.nebula.controller.response.UserAchievementResponse;
import pl.derleta.nebula.domain.model.UserAchievement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility class for mapping UserAchievement domain objects to corresponding UserAchievementResponse DTOs.
 * This class is implemented as a static utility and is not intended to be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserAchievementApiMapper {

    /**
     * Converts a UserAchievement object into a UserAchievementResponse object.
     *
     * @param item the UserAchievement object to be converted
     * @return a UserAchievementResponse object containing data mapped from the given UserAchievement object
     */
    public static UserAchievementResponse toResponse(final UserAchievement item) {
        return UserAchievementResponse.builder()
                .userId(item.userId())
                .achievementId(item.achievementId())
                .value(item.value())
                .level(item.level())
                .progress(item.progress())
                .achievement(item.achievement())
                .build();
    }

    /**
     * Converts a Page object containing UserAchievement entities into a Page object containing UserAchievementResponse objects.
     *
     * @param page the Page object containing UserAchievement entities to be converted
     * @return a Page object containing UserAchievementResponse objects with data mapped from the given UserAchievement entities
     */
    public static Page<UserAchievementResponse> toPageResponse(Page<UserAchievement> page) {
        List<UserAchievementResponse> responseItems = page.getContent().stream()
                .map(UserAchievementApiMapper::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responseItems, page.getPageable(), page.getTotalElements());
    }

}
