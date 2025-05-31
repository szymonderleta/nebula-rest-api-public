package pl.derleta.nebula.controller.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.derleta.nebula.controller.response.NationalityResponse;
import pl.derleta.nebula.controller.response.RegionResponse;
import pl.derleta.nebula.domain.model.Nationality;
import pl.derleta.nebula.domain.model.Region;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class NationalityApiMapperTest {

    private final static String URL_GROUP_PATH = "http://milkyway.local/nebula/res/icon/nationality/";

    @Test
    void toResponse_validNationality_returnsNationalityResponse() {
        // Arrange
        Region region = new Region(1, "Europe");
        Nationality nationality = new Nationality(1, "Polish", "PL", region);

        RegionResponse regionResponse = RegionResponse.builder()
                .id(1)
                .name("Europe")
                .build();

        try (MockedStatic<RegionApiMapper> mockedStatic = Mockito.mockStatic(RegionApiMapper.class)) {
            mockedStatic.when(() -> RegionApiMapper.toResponse(any()))
                    .thenReturn(regionResponse);
            // Act
            NationalityResponse result = NationalityApiMapper.toResponse(nationality);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("Polish", result.getName());
            assertEquals("PL", result.getCode());
            assertEquals(regionResponse, result.getRegion());
            assertEquals(URL_GROUP_PATH + "1.png", result.getImgURL());

            mockedStatic.verify(() -> RegionApiMapper.toResponse(region));
        }
    }

    @Test
    void toResponse_nullNationality_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> NationalityApiMapper.toResponse(null)
        );
    }

    @Test
    void toResponse_nationalityWithNullRegion_throwsNullPointerException() {
        // Arrange
        Nationality nationality = new Nationality(1, "Polish", "PL", null);

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> NationalityApiMapper.toResponse(nationality)
        );
    }

}
