package pl.derleta.nebula.repository.filter;

import org.springframework.data.jpa.domain.Specification;
import pl.derleta.nebula.domain.entity.UserAchievementEntity;

/**
 * A utility class providing specifications for querying {@link UserAchievementEntity}
 * objects based on various criteria related to their achievement levels.
 * <p>
 * This class is designed to be used with Spring Data JPA's {@link Specification}
 * API to create predicates for filtering {@link UserAchievementEntity} instances.
 * <p>
 * All methods in this class are static and immutable.
 */
public final class UserAchievementsSpecifications {

    /**
     * Creates a specification to apply a filter on {@link UserAchievementEntity} objects
     * based on the specified level and filter type.
     * <p>
     * Supported filter types are:
     * - "greater or equal": Filters entities with a level greater than or equal to the specified level.
     * - "less or equal": Filters entities with a level less than or equal to the specified level.
     * - "greater": Filters entities with a level greater than the specified level.
     * - "less": Filters entities with a level less than the specified level.
     * - "notequal": Filters entities with a level not equal to the specified level.
     * - Any other value defaults to filtering entities with a level equal to the specified level.
     *
     * @param level      the reference level for the filtering logic. This value determines
     *                   the behavior of the filter based on the specified filter type.
     * @param filterType the type of filter to be applied. Determines the matching logic
     *                   for {@link UserAchievementEntity} objects based on the level.
     * @return a {@link Specification} that filters {@link UserAchievementEntity} objects
     * according to the specified level and filter type.
     */
    public static Specification<UserAchievementEntity> hasAllFilters(final int level, final String filterType) {
        String filter = (filterType == null) ? "equal" : filterType.toLowerCase();
        return switch (filter) {
            case "greater or equal" -> Specification.where(hasGreaterOrEqualLevel(level));
            case "less or equal" -> Specification.where(hasLessOrEqualLevel(level));
            case "greater" -> Specification.where(hasGreaterLevel(level));
            case "less" -> Specification.where(hasLessLevel(level));
            case "notequal" -> Specification.where(hasNotEqualLevel(level));
            default -> Specification.where(hasEqualLevel(level));
        };
    }

    /**
     * Creates a specification to filter {@link UserAchievementEntity} objects
     * with a level not equal to the specified level.
     *
     * @param level the level to exclude in the returned {@link UserAchievementEntity} objects.
     *              Entities with this level value will not match.
     * @return a {@link Specification} to filter {@link UserAchievementEntity} objects by non-equal level.
     */
    public static Specification<UserAchievementEntity> hasNotEqualLevel(final int level) {
        return (root, query, cb) -> cb.notEqual(root.get("level"), level);
    }

    /**
     * Creates a specification to filter {@link UserAchievementEntity} objects
     * with a level equal to the specified level.
     *
     * @param level the exact level to filter {@link UserAchievementEntity} objects.
     *              Only entities with this level value will match.
     * @return a {@link Specification} to filter {@link UserAchievementEntity} objects by level.
     */
    public static Specification<UserAchievementEntity> hasEqualLevel(final int level) {
        return (root, query, cb) -> cb.equal(root.get("level"), level);
    }

    /**
     * Creates a specification to filter {@link UserAchievementEntity} objects
     * with a level greater than the specified level.
     *
     * @param level the level threshold to filter {@link UserAchievementEntity} objects.
     *              Only entities with a level greater than this value will match.
     * @return a {@link Specification} to filter {@link UserAchievementEntity} objects by level.
     */
    public static Specification<UserAchievementEntity> hasGreaterLevel(final int level) {
        return (root, query, cb) -> cb.greaterThan(root.get("level"), level);
    }

    /**
     * Creates a specification to filter {@link UserAchievementEntity} objects
     * with a level greater than or equal to the specified level.
     *
     * @param level the minimum level to filter {@link UserAchievementEntity} objects.
     *              Only entities with a level greater than or equal to this value will match.
     * @return a {@link Specification} to filter {@link UserAchievementEntity} objects by level.
     */
    public static Specification<UserAchievementEntity> hasGreaterOrEqualLevel(final int level) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("level"), level);
    }

    /**
     * Creates a specification to filter {@link UserAchievementEntity} objects
     * with a level less than the specified level.
     *
     * @param level the level threshold to filter {@link UserAchievementEntity} objects.
     *              Only entities with a level less than this value will match.
     * @return a {@link Specification} to filter {@link UserAchievementEntity} objects by level.
     */
    public static Specification<UserAchievementEntity> hasLessLevel(final int level) {
        return (root, query, cb) -> cb.lessThan(root.get("level"), level);
    }

    /**
     * Creates a specification to filter {@link UserAchievementEntity} objects
     * with a level less than or equal to the specified level.
     *
     * @param level the maximum level to filter {@link UserAchievementEntity} objects.
     *              Only entities with a level less than or equal to this value will match.
     * @return a {@link Specification} to filter {@link UserAchievementEntity} objects by level.
     */
    public static Specification<UserAchievementEntity> hasLessOrEqualLevel(final int level) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("level"), level);
    }

}
