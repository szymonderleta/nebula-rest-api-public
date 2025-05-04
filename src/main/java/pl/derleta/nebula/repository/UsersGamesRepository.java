package pl.derleta.nebula.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.derleta.nebula.domain.entity.UserGameEntity;
import pl.derleta.nebula.domain.entity.id.UsersGameId;

@Repository
public interface UsersGamesRepository extends JpaRepository<UserGameEntity, UsersGameId> {

}
