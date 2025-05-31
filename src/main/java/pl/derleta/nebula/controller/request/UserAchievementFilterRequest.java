package pl.derleta.nebula.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public final class UserAchievementFilterRequest implements Request {

    private int page;
    private int size;
    private String sortBy;
    private String sortOrder;

    private long userId;
    private int level;
    private String filterType;

}
