package pl.derleta.nebula.service;

import pl.derleta.nebula.controller.request.AuthEmailRequest;
import pl.derleta.nebula.controller.request.PasswordUpdateRequest;
import pl.derleta.nebula.controller.request.Request;
import pl.derleta.nebula.controller.request.UserConfirmationRequest;
import pl.derleta.nebula.controller.response.AccountResponse;
import pl.derleta.nebula.controller.response.JwtTokenResponse;

public interface AccountUpdater {

    AccountResponse register(Request request);

    AccountResponse confirm(UserConfirmationRequest confirmation);

    AccountResponse unlock(Long id);

    AccountResponse resetPassword(String email);

    JwtTokenResponse generateToken(AuthEmailRequest authRequest);

    AccountResponse updatePassword(String jwtToken, PasswordUpdateRequest passwordUpdateRequest);

}
