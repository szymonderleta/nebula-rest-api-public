package pl.derleta.nebula.controller.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
@ToString
public class GameResponse extends RepresentationModel<GameResponse> implements Response {

    private Integer id;
    private String name;
    private Boolean enable;
    private String iconUrl;
    private String pageUrl;

}
