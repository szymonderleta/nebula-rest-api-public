package pl.derleta.nebula.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.derleta.nebula.controller.request.AccountRegistrationRequest;
import pl.derleta.nebula.controller.request.AuthServRegistrationRequest;

/**
 * A utility class for mapping {@link AccountRegistrationRequest} objects into {@link AuthServRegistrationRequest} objects.
 * This class is designed to handle the transformation of account registration data, including securely encoding passwords
 * using {@link BCryptPasswordEncoder}.
 * This class is designed to be used as a static utility and cannot be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthServRegistrationRequestMapper {

    /**
     * Transforms an {@link AccountRegistrationRequest} object into an {@link AuthServRegistrationRequest} object.
     * This method encodes the password from the input request using {@link BCryptPasswordEncoder} and populates
     * the resulting {@link AuthServRegistrationRequest} with the encoded password, username, and email from the input.
     *
     * @param request the {@link AccountRegistrationRequest} containing the user's registration information, such as
     *                login, email, and password
     * @return an {@link AuthServRegistrationRequest} object containing the transformed registration data, including
     * the encoded password, username, and email from the input request
     */
    public static AuthServRegistrationRequest getAccountAuthRegistration(AccountRegistrationRequest request) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(request.getPassword());
        return AuthServRegistrationRequest.builder()
                .username(request.getLogin())
                .email(request.getEmail())
                .encryptedPassword(encryptedPassword)
                .build();
    }

}
