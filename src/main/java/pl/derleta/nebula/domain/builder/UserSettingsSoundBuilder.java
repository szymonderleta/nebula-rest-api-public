package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.UserSettingsSound;

public interface UserSettingsSoundBuilder {

    UserSettingsSound build();

    UserSettingsSoundBuilder userId(long userId);

    UserSettingsSoundBuilder muted(boolean muted);

    UserSettingsSoundBuilder battleCry(boolean battleCry);

    UserSettingsSoundBuilder volumeMaster(int volumeMaster);

    UserSettingsSoundBuilder volumeMusic(int volumeMusic);

    UserSettingsSoundBuilder volumeEffects(int volumeEffects);

    UserSettingsSoundBuilder volumeVoices(int volumeVoices);

}
