package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.Theme;
import pl.derleta.nebula.domain.model.UserSettingsGeneral;

public interface UserSettingsGeneralBuilder {

    UserSettingsGeneral build();

    UserSettingsGeneralBuilder userId(long userId);

    UserSettingsGeneralBuilder theme(Theme theme);

}
