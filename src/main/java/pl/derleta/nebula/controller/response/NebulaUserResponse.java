package pl.derleta.nebula.controller.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pl.derleta.nebula.domain.model.*;

import java.sql.Date;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NebulaUserResponse extends RepresentationModel<NebulaUserResponse> implements Response {

    private long id;
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private int age;
    private Date birthDate;
    private Gender gender;
    private Nationality nationality;
    private UserSettings settings;
    private List<Game> games;
    private List<NebulaUserAchievement> achievements;

}
