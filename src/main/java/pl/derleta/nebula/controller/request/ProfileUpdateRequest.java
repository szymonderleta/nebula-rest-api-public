package pl.derleta.nebula.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Builder
@ToString
public final class ProfileUpdateRequest implements Request {

    long userId;
    String firstName;
    String lastName;
    Date birthdate;
    int nationalityId;
    int genderId;

}
