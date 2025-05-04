package pl.derleta.nebula.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.derleta.nebula.domain.entity.id.UserAchievementId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_achievements")
public class UserAchievementEntity {

    @EmbeddedId
    private UserAchievementId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @MapsId("achievementId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "achievement_id", insertable = false, updatable = false)
    private AchievementEntity achievement;

    /**
     * Should represent in percentage value from 0 to 10000,
     * where in example 7632 is equal 76,32%
     */
    @Column(name = "progress")
    private Integer progress;

    @Column(name = "level")
    private Integer level;

    @Column(name = "value")
    private Integer value;

}
