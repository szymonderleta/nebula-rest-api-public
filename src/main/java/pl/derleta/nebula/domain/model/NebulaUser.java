package pl.derleta.nebula.domain.model;

import java.sql.Date;
import java.util.List;

public record NebulaUser(long id, String login, String email, String firstName, String lastName,
                         int age, Date birthDate, Gender gender, Nationality nationality,
                         UserSettings settings, List<Game> games, List<UserAchievement> achievements) {
}
