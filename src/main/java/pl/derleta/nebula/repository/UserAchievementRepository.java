package pl.derleta.nebula.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.derleta.nebula.domain.entity.UserAchievementEntity;
import pl.derleta.nebula.domain.entity.id.UserAchievementId;

import java.util.List;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievementEntity, UserAchievementId> {

    /**
     * Retrieves a paginated list of UserAchievementEntity objects based on the provided specification.
     *
     * @param spec     the specification to filter the UserAchievementEntity objects
     * @param pageable the pagination information
     * @return a paginated list of UserAchievementEntity objects that match the provided specification
     */
    Page<UserAchievementEntity> findAll(Specification<UserAchievementEntity> spec, Pageable pageable);

    /**
     * Retrieves a list of UserAchievementEntity objects for a specified user.
     *
     * @param userId the ID of the user whose achievements are to be retrieved
     * @return a list of UserAchievementEntity objects associated with the specified user ID
     */
    @Query("""
            SELECT ue FROM UserAchievementEntity ue
            WHERE ue.id.userId = :userId
            """)
    List<UserAchievementEntity> getList(Long userId);

    /**
     * Retrieves a UserAchievementEntity based on the provided user ID and achievement ID.
     *
     * @param userId        the ID of the user
     * @param achievementId the ID of the achievement
     * @return the UserAchievementEntity matching the provided user ID and achievement ID, or null if none is found
     */
    @Query("""
            SELECT ue FROM UserAchievementEntity ue
            WHERE ue.id.userId = :userId AND ue.id.achievementId = :achievementId
            """)
    UserAchievementEntity get(Long userId, Integer achievementId);

}
