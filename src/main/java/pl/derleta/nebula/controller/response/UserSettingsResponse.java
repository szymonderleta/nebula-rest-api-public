package pl.derleta.nebula.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import pl.derleta.nebula.domain.model.UserSettingsGeneral;
import pl.derleta.nebula.domain.model.UserSettingsSound;

@Getter
@Builder
@ToString
public class UserSettingsResponse implements Response {

    long userId;
    UserSettingsGeneral general;
    UserSettingsSound sound;

}
