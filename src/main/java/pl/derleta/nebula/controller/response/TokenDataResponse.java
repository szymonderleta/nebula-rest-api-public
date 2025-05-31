package pl.derleta.nebula.controller.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pl.derleta.nebula.domain.rest.Role;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenDataResponse extends RepresentationModel<TokenDataResponse> implements Response {

    boolean valid;
    long userId;
    String email;
    String token;
    Set<Role> roles;

}
