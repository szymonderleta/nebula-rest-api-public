package pl.derleta.nebula.controller.response;

import java.util.Map;

public interface ResponseWithCookieHeaders extends Response {

    void setCookiesHeaders(Map<String, String> headers);

    Map<String, String> getCookiesHeaders();

}
