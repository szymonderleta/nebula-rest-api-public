package pl.derleta.nebula.service;

public interface AuthorizationService {

     boolean notContainsAdminRole(String jwtToken);

}
