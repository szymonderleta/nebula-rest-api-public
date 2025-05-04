package pl.derleta.nebula.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.derleta.nebula.service.AuthorizationService;
import pl.derleta.nebula.service.TokenProvider;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private static final String ADMIN_ROLE = "ROLE_ADMIN";
    private final TokenProvider tokenProvider;

    @Override
    public boolean notContainsAdminRole(String jwtToken) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            return true;
        }

        boolean isValidToken = tokenProvider.isValid(jwtToken);
        if (isValidToken) {
            var roles = tokenProvider.getRoles(jwtToken);
            return roles.stream().noneMatch(role -> ADMIN_ROLE.equals(role.getRoleName()));
        }

        return true;
    }

}
