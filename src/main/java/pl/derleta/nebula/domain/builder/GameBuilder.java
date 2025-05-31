package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.Game;

public interface GameBuilder {

    Game build();

    GameBuilder id(int id);

    GameBuilder name(String name);

    GameBuilder enable(boolean enable);

    GameBuilder iconUrl(String iconUrl);

    GameBuilder pageUrl(String pageUrl);

}
