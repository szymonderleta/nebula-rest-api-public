package pl.derleta.nebula.domain.token;

import lombok.*;
import pl.derleta.nebula.domain.rest.Role;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TokenData {

    boolean valid;
    long userId;
    String email;
    String token;
    Set<Role> roles;

}
