package pl.derleta.nebula.controller.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.derleta.nebula.controller.response.TokenDataResponse;
import pl.derleta.nebula.domain.token.TokenData;

/**
 * A utility class for mapping TokenData domain objects to corresponding TokenDataResponse DTOs.
 * This class is designed to be used as a static utility and is not intended to be instantiated.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TokenDataApiMapper {

    /**
     * Converts a {@link TokenData} object into a {@link TokenDataResponse} object.
     *
     * @param item the TokenData object to be converted, containing validity status, user information,
     *             token details, and associated roles
     * @return a TokenDataResponse object containing the data transformed from the given TokenData object
     */
    public static TokenDataResponse toResponse(final TokenData item) {
        return TokenDataResponse.builder()
                .valid(item.isValid())
                .userId(item.getUserId())
                .email(item.getEmail())
                .token(item.getToken())
                .roles(item.getRoles())
                .build();
    }

}
