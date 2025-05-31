package pl.derleta.nebula.domain.mapper;

import org.junit.jupiter.api.Test;
import pl.derleta.nebula.domain.entity.GenderEntity;
import pl.derleta.nebula.domain.model.Gender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenderMapperTest {

    @Test
    void toGenders_validEntities_returnsGenders() {
        // Arrange
        GenderEntity entity1 = new GenderEntity();
        entity1.setId(1);
        entity1.setName("Male");

        GenderEntity entity2 = new GenderEntity();
        entity2.setId(2);
        entity2.setName("Female");

        List<GenderEntity> entities = Arrays.asList(entity1, entity2);

        // Act
        List<Gender> result = GenderMapper.toGenders(entities);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        Gender gender1 = result.getFirst();
        assertEquals(1, gender1.id());
        assertEquals("Male", gender1.name());
        
        Gender gender2 = result.get(1);
        assertEquals(2, gender2.id());
        assertEquals("Female", gender2.name());
    }

    @Test
    void toGenders_emptyList_returnsEmptyList() {
        // Arrange
        List<GenderEntity> entities = Collections.emptyList();

        // Act
        List<Gender> result = GenderMapper.toGenders(entities);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toGender_validEntity_returnsGender() {
        // Arrange
        GenderEntity entity = new GenderEntity();
        entity.setId(1);
        entity.setName("Male");

        // Act
        Gender result = GenderMapper.toGender(entity);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("Male", result.name());
    }

    @Test
    void toGender_nullEntity_throwsNullPointerException() {
        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> GenderMapper.toGender(null)
        );
    }

    @Test
    void toGender_entityWithNullName_returnsGenderWithNullName() {
        // Arrange
        GenderEntity entity = new GenderEntity();
        entity.setId(1);

        // Act
        Gender result = GenderMapper.toGender(entity);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.id());
        assertNull(result.name());
    }

}
