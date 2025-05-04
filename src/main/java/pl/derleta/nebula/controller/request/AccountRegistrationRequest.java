package pl.derleta.nebula.controller.request;

import lombok.*;

import java.sql.Date;

@Getter
@Builder
@ToString
public final class AccountRegistrationRequest implements Request {

    String login;
    String email;
    String password;
    Date birthdate;
    Integer nationality;
    Integer gender;

}
