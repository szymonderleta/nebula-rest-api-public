package pl.derleta.nebula.controller.request;

import jakarta.validation.constraints.NotNull;

public record UserSettingsSoundRequest(
        @NotNull long userId,
        boolean muted,
        boolean battleCry,
        int volumeMaster,
        int volumeMusic,
        int volumeEffects,
        int volumeVoices) {
}

