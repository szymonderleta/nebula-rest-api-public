package pl.derleta.nebula.service;

import pl.derleta.nebula.domain.model.Nationality;

import java.util.List;

public interface NationalityProvider {

    Nationality get(Integer id);

    List<Nationality> getAll();

}
