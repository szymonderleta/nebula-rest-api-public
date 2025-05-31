package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.Gender;

public interface GenderBuilder {

    Gender build();

    GenderBuilder id(int id);

    GenderBuilder name(String name);

}
