package pl.derleta.nebula.domain.builder.impl;

import pl.derleta.nebula.domain.builder.NationalityBuilder;
import pl.derleta.nebula.domain.model.Nationality;
import pl.derleta.nebula.domain.model.Region;

public final class NationalityBuilderImpl implements NationalityBuilder {

    private int id;
    private String name;
    private String code;
    private Region region;

    @Override
    public NationalityBuilder id(int id) {
        this.id = id;
        return this;
    }

    @Override
    public NationalityBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public NationalityBuilder code(String code) {
        this.code = code;
        return this;
    }

    @Override
    public NationalityBuilder region(Region region) {
        this.region = region;
        return this;
    }

    @Override
    public Nationality build() {
        return new Nationality(id, name, code, region);
    }

}
