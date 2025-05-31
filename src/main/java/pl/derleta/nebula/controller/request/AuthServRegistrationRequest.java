package pl.derleta.nebula.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public final class AuthServRegistrationRequest implements Request {
    String username;
    String email;
    String encryptedPassword;
}
