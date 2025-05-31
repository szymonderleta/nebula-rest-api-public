package pl.derleta.nebula.domain.types;

import lombok.Getter;

/**
 * Enum representing the types of processes related to access token management.
 *
 * <p>This enum defines different process types for handling access tokens, such as refreshing the access token.</p>
 *
 * <ul>
 *     <li>{@link #REFRESH_ACCESS} - Refers to the process of refreshing an access token using a valid refresh token.</li>
 * </ul>
 */
@Getter
public enum AccessProcessType {

    REFRESH_ACCESS(6, "Refresh Access");

    final int id;
    final String name;

    AccessProcessType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "AccessProcessType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
