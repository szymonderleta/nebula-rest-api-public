package pl.derleta.nebula.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RegionResponse implements Response {

    private Integer id;
    private String name;

}
