package pl.derleta.nebula.controller.mapper;

import org.junit.jupiter.api.Test;
import pl.derleta.nebula.controller.response.ThemeResponse;
import pl.derleta.nebula.domain.model.Theme;

import static org.junit.jupiter.api.Assertions.*;

class ThemeApiMapperTest {

    private final static String URL_GROUP_PATH = "http://milkyway.local/nebula/res/icon/theme/";

    @Test
    void toResponse_validTheme_returnsThemeResponse() {
        // Arrange
        Theme theme = new Theme(1, "Dark");

        // Act
        ThemeResponse result = ThemeApiMapper.toResponse(theme);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Dark", result.getName());
        assertEquals(URL_GROUP_PATH + "1.png", result.getImgURL());
    }

    @Test
    void toResponse_nullTheme_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> ThemeApiMapper.toResponse(null)
        );
    }

}
