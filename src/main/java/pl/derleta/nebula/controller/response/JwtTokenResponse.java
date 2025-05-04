package pl.derleta.nebula.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class JwtTokenResponse implements ResponseWithCookieHeaders {

    @JsonIgnore
    private Map<String, String>  cookiesHeaders;

    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;

    @Override
    public void setCookiesHeaders(Map<String, String> headers) {
        cookiesHeaders = headers;
    }

    @Override
    public Map<String, String> getCookiesHeaders() {
        return cookiesHeaders;
    }

}
