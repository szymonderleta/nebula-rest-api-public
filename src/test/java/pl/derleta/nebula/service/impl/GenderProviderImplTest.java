package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.domain.entity.GenderEntity;
import pl.derleta.nebula.domain.model.Gender;
import pl.derleta.nebula.exceptions.GenderNotFoundException;
import pl.derleta.nebula.repository.GenderRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class GenderProviderImplTest {

    @Mock
    private GenderRepository repository;

    @InjectMocks
    private GenderProviderImpl genderProvider;

    private GenderEntity testGenderEntity1;
    private GenderEntity testGenderEntity2;
    private Gender testGender1;
    private Gender testGender2;

    @BeforeEach
    void setUp() {
        testGenderEntity1 = new GenderEntity();
        testGenderEntity1.setId(1);
        testGenderEntity1.setName("Male");

        testGenderEntity2 = new GenderEntity();
        testGenderEntity2.setId(2);
        testGenderEntity2.setName("Female");

        testGender1 = new Gender(1, "Male");
        testGender2 = new Gender(2, "Female");
    }

    @Test
    void get_shouldReturnGender_whenValidIdProvided() {
        // Arrange
        when(repository.findById(1)).thenReturn(Optional.of(testGenderEntity1));

        // Act
        Gender result = genderProvider.get(1);

        // Assert
        assertNotNull(result);
        assertEquals(testGender1.id(), result.id());
        assertEquals(testGender1.name(), result.name());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void get_shouldThrowIllegalArgumentException_whenNullIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Integer nullId = null;
            genderProvider.get(nullId);
        });
        assertEquals("Gender ID cannot be null", exception.getMessage());
        verify(repository, never()).findById(anyInt());
    }

    @Test
    void get_shouldThrowGenderNotFoundException_whenGenderNotFound() {
        // Arrange
        when(repository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        GenderNotFoundException exception = assertThrows(GenderNotFoundException.class, () -> {
            genderProvider.get(999);
        });
        assertEquals("Gender with id: 999 not found", exception.getMessage());
        verify(repository, times(1)).findById(999);
    }

    @Test
    void getAll_shouldReturnListOfGenders() {
        // Arrange
        List<GenderEntity> genderEntities = Arrays.asList(testGenderEntity1, testGenderEntity2);
        when(repository.findAll()).thenReturn(genderEntities);

        // Act
        List<Gender> result = genderProvider.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testGender1.id(), result.get(0).id());
        assertEquals(testGender1.name(), result.get(0).name());
        assertEquals(testGender2.id(), result.get(1).id());
        assertEquals(testGender2.name(), result.get(1).name());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoGendersExist() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Gender> result = genderProvider.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

}
