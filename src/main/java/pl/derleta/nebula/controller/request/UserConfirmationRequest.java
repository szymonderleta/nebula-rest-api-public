package pl.derleta.nebula.controller.request;

public record UserConfirmationRequest(Long tokenId, String token) implements Request {
}
