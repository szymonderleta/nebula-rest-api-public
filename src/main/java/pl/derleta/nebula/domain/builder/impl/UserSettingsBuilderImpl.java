package pl.derleta.nebula.domain.builder.impl;

import pl.derleta.nebula.domain.builder.UserSettingsBuilder;
import pl.derleta.nebula.domain.model.UserSettings;
import pl.derleta.nebula.domain.model.UserSettingsGeneral;
import pl.derleta.nebula.domain.model.UserSettingsSound;

public final class UserSettingsBuilderImpl implements UserSettingsBuilder {

    private long userId;
    private UserSettingsGeneral userSettingsGeneral;
    private UserSettingsSound userSettingsSound;

    @Override
    public UserSettingsBuilder userId(long userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public UserSettingsBuilder userSettingsGeneral(UserSettingsGeneral userSettingsGeneral) {
        this.userSettingsGeneral = userSettingsGeneral;
        return this;
    }

    @Override
    public UserSettingsBuilder userSettingsSound(UserSettingsSound userSettingsSound) {
        this.userSettingsSound = userSettingsSound;
        return this;
    }

    @Override
    public UserSettings build() {
        return new UserSettings(userId, userSettingsGeneral, userSettingsSound);
    }

}
