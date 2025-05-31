package pl.derleta.nebula.domain.builder.impl;

import pl.derleta.nebula.domain.builder.GenderBuilder;
import pl.derleta.nebula.domain.model.Gender;

public final class GenderBuilderImpl implements GenderBuilder {

    private int id;
    private String name;

    @Override
    public GenderBuilder id(int id) {
        this.id = id;
        return this;
    }

    @Override
    public GenderBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Gender build() {
        return new Gender(id, name);
    }

}
