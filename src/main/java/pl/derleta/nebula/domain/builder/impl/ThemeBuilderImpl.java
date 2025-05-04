package pl.derleta.nebula.domain.builder.impl;

import pl.derleta.nebula.domain.builder.ThemeBuilder;
import pl.derleta.nebula.domain.model.Theme;

public final class ThemeBuilderImpl implements ThemeBuilder {

    private int id;
    private String name;

    @Override
    public ThemeBuilder id(int id) {
        this.id = id;
        return this;
    }

    @Override
    public ThemeBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Theme build() {
        return new Theme(id, name);
    }

}
