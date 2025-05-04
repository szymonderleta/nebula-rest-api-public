package pl.derleta.nebula.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public final class GameFilterRequest implements Request {

    private int page;
    private int size;
    private String sortBy;
    private String sortOrder;

    private String name;
    private Boolean enable;

}
