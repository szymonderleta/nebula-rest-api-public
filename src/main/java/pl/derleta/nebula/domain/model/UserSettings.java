package pl.derleta.nebula.domain.model;

public record UserSettings(
        long userId,
        UserSettingsGeneral general,
        UserSettingsSound sound) {
}

