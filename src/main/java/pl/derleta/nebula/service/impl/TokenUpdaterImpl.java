package pl.derleta.nebula.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.derleta.nebula.controller.response.Response;
import pl.derleta.nebula.service.TokenUpdater;
import pl.derleta.nebula.util.HttpAuthClient;

/**
 * An implementation of the {@link TokenUpdater} interface, responsible for handling
 * operations related to JWT tokens, such as requesting a new access token.
 */
@Service
@RequiredArgsConstructor
public class TokenUpdaterImpl implements TokenUpdater {

    final HttpAuthClient httpAuthServClient;

    @Override
    public Response refreshAccess(final String token) {
        var response = httpAuthServClient.refreshAccess(token);
        System.out.println(response);
        return response;
//        return httpAuthServClient.refreshAccess(token);
    }

}
