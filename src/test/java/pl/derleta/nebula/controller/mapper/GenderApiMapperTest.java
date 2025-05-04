package pl.derleta.nebula.controller.mapper;

import org.junit.jupiter.api.Test;
import pl.derleta.nebula.controller.response.GenderResponse;
import pl.derleta.nebula.domain.model.Gender;

import static org.junit.jupiter.api.Assertions.*;

class GenderApiMapperTest {

    private final static String URL_GROUP_PATH = "http://milkyway.local/nebula/res/icon/gender/";

    @Test
    void toResponse_validGender_returnsGenderResponse() {
        // Arrange
        Gender gender = new Gender(1, "Male");

        // Act
        GenderResponse result = GenderApiMapper.toResponse(gender);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Male", result.getName());
        assertEquals(URL_GROUP_PATH + "1.png", result.getImgURL());
    }

    @Test
    void toResponse_nullGender_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> GenderApiMapper.toResponse(null)
        );
    }

}
