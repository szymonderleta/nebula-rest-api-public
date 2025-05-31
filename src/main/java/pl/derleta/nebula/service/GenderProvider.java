package pl.derleta.nebula.service;

import pl.derleta.nebula.domain.model.Gender;

import java.util.List;

public interface GenderProvider {

    List<Gender> getAll();

    Gender get(Integer id);

}
