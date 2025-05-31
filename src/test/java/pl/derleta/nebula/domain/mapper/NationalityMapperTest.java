package pl.derleta.nebula.domain.mapper;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.derleta.nebula.domain.entity.NationalityEntity;
import pl.derleta.nebula.domain.entity.RegionEntity;
import pl.derleta.nebula.domain.model.Nationality;
import pl.derleta.nebula.domain.model.Region;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class NationalityMapperTest {

    @Test
    void toNationalities_validEntities_returnsNationalities() {
        // Arrange
        RegionEntity regionEntity1 = new RegionEntity();
        regionEntity1.setId(1);
        regionEntity1.setName("Europe");

        RegionEntity regionEntity2 = new RegionEntity();
        regionEntity2.setId(2);
        regionEntity2.setName("Asia");

        NationalityEntity entity1 = new NationalityEntity();
        entity1.setId(1);
        entity1.setName("Polish");
        entity1.setCode("PL");
        entity1.setRegion(regionEntity1);

        NationalityEntity entity2 = new NationalityEntity();
        entity2.setId(2);
        entity2.setName("Japanese");
        entity2.setCode("JP");
        entity2.setRegion(regionEntity2);

        List<NationalityEntity> entities = Arrays.asList(entity1, entity2);

        Region region1 = new Region(1, "Europe");
        Region region2 = new Region(2, "Asia");

        try (MockedStatic<RegionMapper> mockedStatic = Mockito.mockStatic(RegionMapper.class)) {
            mockedStatic.when(() -> RegionMapper.toRegion(regionEntity1))
                    .thenReturn(region1);
            mockedStatic.when(() -> RegionMapper.toRegion(regionEntity2))
                    .thenReturn(region2);

            // Act
            List<Nationality> result = NationalityMapper.toNationalities(entities);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            
            Nationality nationality1 = result.getFirst();
            assertEquals(1, nationality1.id());
            assertEquals("Polish", nationality1.name());
            assertEquals("PL", nationality1.code());
            assertEquals(region1, nationality1.region());
            
            Nationality nationality2 = result.get(1);
            assertEquals(2, nationality2.id());
            assertEquals("Japanese", nationality2.name());
            assertEquals("JP", nationality2.code());
            assertEquals(region2, nationality2.region());

            mockedStatic.verify(() -> RegionMapper.toRegion(regionEntity1));
            mockedStatic.verify(() -> RegionMapper.toRegion(regionEntity2));
        }
    }

    @Test
    void toNationalities_emptyList_returnsEmptyList() {
        // Arrange
        List<NationalityEntity> entities = Collections.emptyList();

        // Act
        List<Nationality> result = NationalityMapper.toNationalities(entities);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toNationality_validEntity_returnsNationality() {
        // Arrange
        RegionEntity regionEntity = new RegionEntity();
        regionEntity.setId(1);
        regionEntity.setName("Europe");

        NationalityEntity entity = new NationalityEntity();
        entity.setId(1);
        entity.setName("Polish");
        entity.setCode("PL");
        entity.setRegion(regionEntity);

        Region region = new Region(1, "Europe");

        try (MockedStatic<RegionMapper> mockedStatic = Mockito.mockStatic(RegionMapper.class)) {
            mockedStatic.when(() -> RegionMapper.toRegion(any()))
                    .thenReturn(region);

            // Act
            Nationality result = NationalityMapper.toNationality(entity);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.id());
            assertEquals("Polish", result.name());
            assertEquals("PL", result.code());
            assertEquals(region, result.region());
            mockedStatic.verify(() -> RegionMapper.toRegion(regionEntity));
        }
    }

    @Test
    void toNationality_nullEntity_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> NationalityMapper.toNationality(null)
        );
    }

    @Test
    void toNationality_entityWithNullFields_returnsNationalityWithNullFields() {
        // Arrange
        NationalityEntity entity = new NationalityEntity();
        entity.setId(1);

        try (MockedStatic<RegionMapper> mockedStatic = Mockito.mockStatic(RegionMapper.class)) {
            mockedStatic.when(() -> RegionMapper.toRegion(any()))
                    .thenReturn(null);

            // Act
            Nationality result = NationalityMapper.toNationality(entity);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.id());
            assertNull(result.name());
            assertNull(result.code());
            assertNull(result.region());

            mockedStatic.verify(() -> RegionMapper.toRegion(null));
        }
    }

}
