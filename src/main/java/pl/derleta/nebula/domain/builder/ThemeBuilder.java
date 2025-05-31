package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.Theme;

public interface ThemeBuilder {

    Theme build();

    ThemeBuilder id(int id);

    ThemeBuilder name(String name);

}
