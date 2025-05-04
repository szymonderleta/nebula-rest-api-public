package pl.derleta.nebula.domain.builder.impl;

import pl.derleta.nebula.domain.builder.TokenDataBuilder;
import pl.derleta.nebula.domain.model.Gender;
import pl.derleta.nebula.domain.rest.Role;
import pl.derleta.nebula.domain.token.TokenData;

import java.util.Set;

public final class TokenDataBuilderImpl implements TokenDataBuilder {

    boolean valid;
    long userId;
    String email;
    String token;
    Set<Role> roles;

    @Override
    public TokenData build() {
        return new TokenData(valid, userId, email, token, roles);
    }

    @Override
    public TokenDataBuilder valid(boolean valid) {
        this.valid = valid;
        return this;
    }

    @Override
    public TokenDataBuilder userId(long userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public TokenDataBuilder email(String email) {
        this.email = email;
        return this;
    }

    @Override
    public TokenDataBuilder token(String token) {
        this.token = token;
        return this;
    }

    @Override
    public TokenDataBuilder roles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

}
