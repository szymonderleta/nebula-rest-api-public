package pl.derleta.nebula.controller.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class GenderResponse extends RepresentationModel<GenderResponse> implements Response {

    private Integer id;
    private String name;
    private String imgURL;

}
