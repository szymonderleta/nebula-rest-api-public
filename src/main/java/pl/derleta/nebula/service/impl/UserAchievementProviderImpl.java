package pl.derleta.nebula.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.derleta.nebula.controller.request.UserAchievementFilterRequest;
import pl.derleta.nebula.domain.entity.UserAchievementEntity;
import pl.derleta.nebula.domain.mapper.UserAchievementMapper;
import pl.derleta.nebula.domain.model.UserAchievement;
import pl.derleta.nebula.repository.UserAchievementRepository;
import pl.derleta.nebula.repository.filter.UserAchievementsSpecifications;
import pl.derleta.nebula.service.UserAchievementProvider;

import java.util.List;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

/**
 * Implementation of the UserAchievementProvider interface, responsible for providing user achievement
 * data and related operations such as retrieval by user ID, achievement ID, and pagination.
 */
@AllArgsConstructor
@Service
public class UserAchievementProviderImpl implements UserAchievementProvider {

    private final UserAchievementRepository repository;

    /**
     * Retrieves a UserAchievement for the specified user ID and achievement ID.
     *
     * @param userId the ID of the user
     * @param achievementId the ID of the achievement
     * @return a UserAchievement object if found, or null if no achievement exists for the given IDs
     */
    @Override
    public UserAchievement get(Long userId, Integer achievementId) {
        UserAchievementEntity entity = repository.get(userId, achievementId);
        if (entity == null) {
            return null;
        }
        return UserAchievementMapper.toUserAchievement(entity);
    }

    /**
     * Retrieves a list of user achievements for the specified user ID.
     *
     * @param userId the ID of the user whose achievements are to be retrieved
     * @return a list of UserAchievement objects representing the user's achievements
     */
    @Transactional
    @Override
    public List<UserAchievement> getList(final Long userId) {
        List<UserAchievementEntity> list = repository.getList(userId);
        return list.stream().map(UserAchievementMapper::toUserAchievement).collect(Collectors.toList());
    }

    /**
     * Retrieves a paginated list of UserAchievements based on the provided filter request.
     *
     * @param request the UserAchievementFilterRequest containing pagination, sorting, and filtering criteria
     * @return a Page object containing UserAchievement objects that match the specified criteria
     */
    @Transactional
    @Override
    public Page<UserAchievement> getPage(UserAchievementFilterRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortOrder()), request.getSortBy()));
        Specification<UserAchievementEntity> spec = UserAchievementsSpecifications.hasAllFilters(request.getLevel(), request.getFilterType());
        return getPage(repository.findAll(spec, pageable));
    }

    /**
     * Converts a Page of UserAchievementEntity objects into a Page of UserAchievement objects.
     *
     * @param entitiesPage the Page containing UserAchievementEntity objects to be converted
     * @return a Page of UserAchievement objects corresponding to the input entities
     */
    private Page<UserAchievement> getPage(final Page<UserAchievementEntity> entitiesPage) {
        List<UserAchievement> collection = entitiesPage
                .stream()
                .map(UserAchievementMapper::toUserAchievement)
                .collect(Collectors.toList());
        LongSupplier totalElements = entitiesPage::getTotalElements;
        return PageableExecutionUtils.getPage(collection, entitiesPage.getPageable(), totalElements);
    }

}
