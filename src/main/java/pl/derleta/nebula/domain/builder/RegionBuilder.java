package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.Region;

public interface RegionBuilder {

    Region build();

    RegionBuilder id(int id);

    RegionBuilder name(String name);

}
