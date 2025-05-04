package pl.derleta.nebula.controller.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pl.derleta.nebula.domain.model.Achievement;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAchievementResponse extends RepresentationModel<UserAchievementResponse> implements Response {

    private long userId;
    private int achievementId;
    private int value;
    private int level;
    private String progress;
    private Achievement achievement;

}
