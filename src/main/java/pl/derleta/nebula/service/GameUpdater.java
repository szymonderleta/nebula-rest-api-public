package pl.derleta.nebula.service;

import org.springframework.http.ResponseEntity;
import pl.derleta.nebula.domain.model.Game;

public interface GameUpdater {

    Game create(Game game);

    Game update(Game game);

    ResponseEntity<String> delete(int id);

}
