package pl.derleta.nebula.service;


import pl.derleta.nebula.controller.response.Response;

public interface TokenUpdater {

    Response refreshAccess(String token);

}
