package pl.derleta.nebula.domain.builder.impl;

import pl.derleta.nebula.domain.builder.UserSettingsGeneralBuilder;
import pl.derleta.nebula.domain.model.Theme;
import pl.derleta.nebula.domain.model.UserSettingsGeneral;

public final class UserSettingsGeneralBuilderImpl implements UserSettingsGeneralBuilder {

    private long userId;
    private Theme theme;

    @Override
    public UserSettingsGeneralBuilder userId(long userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public UserSettingsGeneralBuilder theme(Theme theme) {
        this.theme = theme;
        return this;
    }

    @Override
    public UserSettingsGeneral build() {
        return new UserSettingsGeneral(userId, theme);
    }

}
