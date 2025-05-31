package pl.derleta.nebula.controller.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NationalityResponse extends RepresentationModel<NationalityResponse> implements Response {

    private Integer id;
    private String name;
    private String code;
    private RegionResponse region;
    private String imgURL;
}
