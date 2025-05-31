package pl.derleta.nebula.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserSettingsRequest(
        @JsonProperty("userId") long userId,
        @JsonProperty("general") UserSettingsGeneralRequest general,
        @JsonProperty("sound") UserSettingsSoundRequest sound) {
}
