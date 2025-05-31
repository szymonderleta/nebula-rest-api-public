package pl.derleta.nebula.domain.mapper;

import org.junit.jupiter.api.Test;
import pl.derleta.nebula.domain.entity.RegionEntity;
import pl.derleta.nebula.domain.model.Region;

import static org.junit.jupiter.api.Assertions.*;

class RegionMapperTest {

    @Test
    void toRegion_validEntity_returnsRegion() {
        // Arrange
        RegionEntity entity = new RegionEntity();
        entity.setId(1);
        entity.setName("Europe");

        // Act
        Region result = RegionMapper.toRegion(entity);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Europe", result.name());
    }

    @Test
    void toRegion_nullEntity_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> RegionMapper.toRegion(null)
        );
    }

    @Test
    void toRegion_entityWithNullName_returnsRegionWithNullName() {
        // Arrange
        RegionEntity entity = new RegionEntity();
        entity.setId(1);
        // Name is null

        // Act
        Region result = RegionMapper.toRegion(entity);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.id());
        assertNull(result.name());
    }

}
