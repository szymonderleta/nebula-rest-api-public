package pl.derleta.nebula.controller.mapper;

import org.junit.jupiter.api.Test;
import pl.derleta.nebula.controller.response.RegionResponse;
import pl.derleta.nebula.domain.model.Region;

import static org.junit.jupiter.api.Assertions.*;

class RegionApiMapperTest {

    @Test
    void toResponse_validRegion_returnsRegionResponse() {
        // Arrange
        Region region = new Region(1, "Europe");

        // Act
        RegionResponse result = RegionApiMapper.toResponse(region);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Europe", result.getName());
    }

    @Test
    void toResponse_nullRegion_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> RegionApiMapper.toResponse(null)
        );
    }

}
