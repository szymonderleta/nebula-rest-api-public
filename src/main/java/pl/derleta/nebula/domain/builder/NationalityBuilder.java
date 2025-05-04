package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.Nationality;
import pl.derleta.nebula.domain.model.Region;

public interface NationalityBuilder {

    Nationality build();

    NationalityBuilder id(int id);

    NationalityBuilder name(String name);

    NationalityBuilder code(String code);

    NationalityBuilder region(Region region);

}
