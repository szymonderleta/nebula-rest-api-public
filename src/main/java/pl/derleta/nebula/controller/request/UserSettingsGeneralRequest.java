package pl.derleta.nebula.controller.request;

import jakarta.validation.constraints.NotNull;
import pl.derleta.nebula.domain.model.Theme;

public record UserSettingsGeneralRequest(
        @NotNull long userId,
        @NotNull Theme theme) {
}
