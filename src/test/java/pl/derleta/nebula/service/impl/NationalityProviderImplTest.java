package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.domain.entity.NationalityEntity;
import pl.derleta.nebula.domain.entity.RegionEntity;
import pl.derleta.nebula.domain.model.Nationality;
import pl.derleta.nebula.domain.model.Region;
import pl.derleta.nebula.exceptions.NationalityNotFoundException;
import pl.derleta.nebula.repository.NationalityRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class NationalityProviderImplTest {

    @Mock
    private NationalityRepository repository;

    @InjectMocks
    private NationalityProviderImpl nationalityProvider;

    private NationalityEntity testNationalityEntity1;
    private NationalityEntity testNationalityEntity2;
    private Nationality testNationality1;
    private Nationality testNationality2;

    @BeforeEach
    void setUp() {
        // Set up test region
        RegionEntity testRegionEntity = new RegionEntity();
        testRegionEntity.setId(1);
        testRegionEntity.setName("Europe");

        Region testRegion = new Region(1, "Europe");

        // Set up test nationality entities
        testNationalityEntity1 = new NationalityEntity();
        testNationalityEntity1.setId(1);
        testNationalityEntity1.setName("United States");
        testNationalityEntity1.setCode("USA");
        testNationalityEntity1.setRegion(testRegionEntity);

        testNationalityEntity2 = new NationalityEntity();
        testNationalityEntity2.setId(2);
        testNationalityEntity2.setName("Canada");
        testNationalityEntity2.setCode("CAN");
        testNationalityEntity2.setRegion(testRegionEntity);

        // Set up test nationality models
        testNationality1 = new Nationality(1, "United States", "USA", testRegion);
        testNationality2 = new Nationality(2, "Canada", "CAN", testRegion);
    }

    @Test
    void get_shouldReturnNationality_whenValidIdProvided() {
        // Arrange
        when(repository.findById(1)).thenReturn(Optional.of(testNationalityEntity1));

        // Act
        Nationality result = nationalityProvider.get(1);

        // Assert
        assertNotNull(result);
        assertEquals(testNationality1.id(), result.id());
        assertEquals(testNationality1.name(), result.name());
        assertEquals(testNationality1.code(), result.code());
        assertEquals(testNationality1.region().id(), result.region().id());
        assertEquals(testNationality1.region().name(), result.region().name());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void get_shouldThrowIllegalArgumentException_whenNullIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Integer nullId = null;
            nationalityProvider.get(nullId);
        });
        assertEquals("Nationality ID cannot be null", exception.getMessage());
        verify(repository, never()).findById(anyInt());
    }

    @Test
    void get_shouldThrowNationalityNotFoundException_whenNationalityNotFound() {
        // Arrange
        when(repository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        NationalityNotFoundException exception = assertThrows(NationalityNotFoundException.class, () -> nationalityProvider.get(999));
        assertEquals("Nationality with id: 999 not found", exception.getMessage());
        verify(repository, times(1)).findById(999);
    }

    @Test
    void getAll_shouldReturnListOfNationalities() {
        // Arrange
        List<NationalityEntity> nationalityEntities = Arrays.asList(testNationalityEntity1, testNationalityEntity2);
        when(repository.findAll()).thenReturn(nationalityEntities);

        // Act
        List<Nationality> result = nationalityProvider.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testNationality1.id(), result.getFirst().id());
        assertEquals(testNationality1.name(), result.getFirst().name());
        assertEquals(testNationality1.code(), result.getFirst().code());
        assertEquals(testNationality1.region().id(), result.getFirst().region().id());
        assertEquals(testNationality1.region().name(), result.getFirst().region().name());
        assertEquals(testNationality2.id(), result.get(1).id());
        assertEquals(testNationality2.name(), result.get(1).name());
        assertEquals(testNationality2.code(), result.get(1).code());
        assertEquals(testNationality2.region().id(), result.get(1).region().id());
        assertEquals(testNationality2.region().name(), result.get(1).region().name());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoNationalitiesExist() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Nationality> result = nationalityProvider.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

}
