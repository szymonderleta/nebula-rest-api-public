package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.rest.Role;
import pl.derleta.nebula.domain.token.TokenData;

import java.util.Set;

public interface TokenDataBuilder {

    TokenData build();

    TokenDataBuilder valid(boolean valid);

    TokenDataBuilder userId(long userId);

    TokenDataBuilder email(String email);

    TokenDataBuilder token(String token);

    TokenDataBuilder roles(Set<Role> roles);

}
