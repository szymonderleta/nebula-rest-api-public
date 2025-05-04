package pl.derleta.nebula.controller.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ThemeResponse extends RepresentationModel<ThemeResponse> implements Response {

    private Integer id;
    private String name;
    private String imgURL;

}
