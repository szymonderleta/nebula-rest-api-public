package pl.derleta.nebula.domain.builder;

import pl.derleta.nebula.domain.model.*;

import java.sql.Date;
import java.util.List;

public interface NebulaUserBuilder {

    NebulaUser build();

    NebulaUserBuilder id(long id);

    NebulaUserBuilder login(String login);

    NebulaUserBuilder email(String email);

    NebulaUserBuilder firstName(String firstName);

    NebulaUserBuilder lastName(String lastName);

    NebulaUserBuilder age(int age);

    NebulaUserBuilder birthDate(Date birthDate);

    NebulaUserBuilder gender(Gender gender);

    NebulaUserBuilder nationality(Nationality nationality);

    NebulaUserBuilder settings(UserSettings settings);

    NebulaUserBuilder games(List<Game> games);

    NebulaUserBuilder achievements(List<UserAchievement> achievements);

}
