package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.entity.GameEntity;

public interface GameEntityBuilder {

    GameEntity build();

    GameEntityBuilder id(int id);

    GameEntityBuilder name(String name);

    GameEntityBuilder enable(boolean enable);

    GameEntityBuilder iconUrl(String iconUrl);

    GameEntityBuilder pageUrl(String pageUrl);

}
