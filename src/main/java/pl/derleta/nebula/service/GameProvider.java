package pl.derleta.nebula.service;

import org.springframework.data.domain.Page;
import pl.derleta.nebula.controller.request.GameFilterRequest;
import pl.derleta.nebula.domain.model.Game;

import java.util.List;

public interface GameProvider {

    Game get(Integer id);

    Page<Game> get(GameFilterRequest request);

    int getNextId();

    List<Game> getEnabled();

}
