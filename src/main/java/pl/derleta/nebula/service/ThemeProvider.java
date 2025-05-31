package pl.derleta.nebula.service;

import pl.derleta.nebula.domain.model.Theme;

import java.util.List;

public interface ThemeProvider {

    List<Theme> getAll();

    Theme get(Integer id);

}
