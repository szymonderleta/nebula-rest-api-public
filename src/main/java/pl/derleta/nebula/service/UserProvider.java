package pl.derleta.nebula.service;

import pl.derleta.nebula.domain.model.NebulaUser;

public interface UserProvider {

    NebulaUser get(Long userId);

}
