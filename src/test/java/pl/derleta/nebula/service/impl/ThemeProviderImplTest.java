package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.domain.entity.ThemeEntity;
import pl.derleta.nebula.domain.model.Theme;
import pl.derleta.nebula.exceptions.ThemeNotFoundException;
import pl.derleta.nebula.repository.ThemeRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class ThemeProviderImplTest {

    @Mock
    private ThemeRepository repository;

    @InjectMocks
    private ThemeProviderImpl themeProvider;

    private ThemeEntity testThemeEntity1;
    private ThemeEntity testThemeEntity2;
    private Theme testTheme1;
    private Theme testTheme2;

    @BeforeEach
    void setUp() {
        // Set up test theme entities
        testThemeEntity1 = new ThemeEntity();
        testThemeEntity1.setId(1);
        testThemeEntity1.setName("Dark");

        testThemeEntity2 = new ThemeEntity();
        testThemeEntity2.setId(2);
        testThemeEntity2.setName("Light");

        // Set up test theme models
        testTheme1 = new Theme(1, "Dark");
        testTheme2 = new Theme(2, "Light");
    }

    @Test
    void get_shouldReturnTheme_whenValidIdProvided() {
        // Arrange
        when(repository.findById(1)).thenReturn(Optional.of(testThemeEntity1));

        // Act
        Theme result = themeProvider.get(1);

        // Assert
        assertNotNull(result);
        assertEquals(testTheme1.id(), result.id());
        assertEquals(testTheme1.name(), result.name());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void get_shouldThrowIllegalArgumentException_whenNullIdProvided() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Integer nullId = null;
            themeProvider.get(nullId);
        });
        assertEquals("Theme ID cannot be null", exception.getMessage());
        verify(repository, never()).findById(anyInt());
    }

    @Test
    void get_shouldThrowThemeNotFoundException_whenThemeNotFound() {
        // Arrange
        when(repository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        ThemeNotFoundException exception = assertThrows(ThemeNotFoundException.class, () -> themeProvider.get(999));
        assertEquals("Theme with id: 999 not found", exception.getMessage());
        verify(repository, times(1)).findById(999);
    }

    @Test
    void getAll_shouldReturnListOfThemes() {
        // Arrange
        List<ThemeEntity> themeEntities = Arrays.asList(testThemeEntity1, testThemeEntity2);
        when(repository.findAll()).thenReturn(themeEntities);

        // Act
        List<Theme> result = themeProvider.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testTheme1.id(), result.get(0).id());
        assertEquals(testTheme1.name(), result.get(0).name());
        assertEquals(testTheme2.id(), result.get(1).id());
        assertEquals(testTheme2.name(), result.get(1).name());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoThemesExist() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Theme> result = themeProvider.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

}
