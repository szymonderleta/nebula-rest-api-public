package pl.derleta.nebula.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import pl.derleta.nebula.domain.types.AccountResponseType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public final class AccountResponse implements Response {

    @JsonProperty("success")
    boolean success;
    @JsonProperty("type")
    AccountResponseType type;

}
