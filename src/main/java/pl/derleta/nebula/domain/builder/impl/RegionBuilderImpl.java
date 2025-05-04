package pl.derleta.nebula.domain.builder.impl;

import pl.derleta.nebula.domain.builder.RegionBuilder;
import pl.derleta.nebula.domain.model.Region;

public final class RegionBuilderImpl implements RegionBuilder {

    private int id;
    private String name;

    @Override
    public RegionBuilder id(int id) {
        this.id = id;
        return this;
    }

    @Override
    public RegionBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Region build() {
        return new Region(id, name);
    }

}
