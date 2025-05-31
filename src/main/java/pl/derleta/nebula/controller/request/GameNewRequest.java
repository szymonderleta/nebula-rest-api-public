package pl.derleta.nebula.controller.request;

public record GameNewRequest(String name,
                             Boolean enable,
                             String iconUrl,
                             String pageUrl) implements Request {

}
