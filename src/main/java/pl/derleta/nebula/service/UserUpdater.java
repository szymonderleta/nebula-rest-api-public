package pl.derleta.nebula.service;

import pl.derleta.nebula.controller.request.Request;
import pl.derleta.nebula.domain.model.NebulaUser;
import pl.derleta.nebula.domain.model.UserSettings;

public interface UserUpdater {

    NebulaUser updateProfile(Request request);

    UserSettings updateSettings(UserSettings userSettings);

}
