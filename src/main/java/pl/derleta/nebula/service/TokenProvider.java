package pl.derleta.nebula.service;

import pl.derleta.nebula.domain.rest.Role;
import pl.derleta.nebula.domain.token.TokenData;

import java.util.Set;

public interface TokenProvider {

    TokenData getTokenData(String token);

    boolean isValid(String token);

    boolean isValid(String authorizationHeader, long userId);

    Long getUserId(String token);

    String getEmail(String token);

    Set<Role> getRoles(String token);

}
