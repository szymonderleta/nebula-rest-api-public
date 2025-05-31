package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.UserSettings;
import pl.derleta.nebula.domain.model.UserSettingsGeneral;
import pl.derleta.nebula.domain.model.UserSettingsSound;

public interface UserSettingsBuilder {

    UserSettings build();

    UserSettingsBuilder userId(long userId);

    UserSettingsBuilder userSettingsGeneral(UserSettingsGeneral userSettingsGeneral);

    UserSettingsBuilder userSettingsSound(UserSettingsSound userSettingsSound);

}
