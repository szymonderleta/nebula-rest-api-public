package pl.derleta.nebula.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.derleta.nebula.domain.entity.id.UsersGameId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_games")
public class UserGameEntity {

    @EmbeddedId
    private UsersGameId id;

}
