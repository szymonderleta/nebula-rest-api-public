package pl.derleta.nebula.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.derleta.nebula.controller.assembler.ThemeModelAssembler;
import pl.derleta.nebula.controller.response.ThemeResponse;
import pl.derleta.nebula.domain.model.Theme;
import pl.derleta.nebula.service.ThemeProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThemeControllerTest {

    @Mock
    private ThemeProvider themeProvider;

    @Mock
    private ThemeModelAssembler modelAssembler;

    @InjectMocks
    private ThemeController themeController;

    private Theme darkTheme;
    private Theme lightTheme;
    private ThemeResponse darkThemeResponse;
    private ThemeResponse lightThemeResponse;
    private List<Theme> themeList;
    private Collection<ThemeResponse> themeResponseCollection;

    @BeforeEach
    void setUp() {
        // Set up test data
        darkTheme = new Theme(1, "Dark");
        lightTheme = new Theme(2, "Light");
        themeList = Arrays.asList(darkTheme, lightTheme);

        darkThemeResponse = ThemeResponse.builder()
                .id(darkTheme.id())
                .name(darkTheme.name())
                .build();

        lightThemeResponse = ThemeResponse.builder()
                .id(lightTheme.id())
                .name(lightTheme.name())
                .build();

        themeResponseCollection = Arrays.asList(darkThemeResponse, lightThemeResponse);
    }

    @Test
    void get_validId_returnsThemeResponse() {
        // Arrange
        Integer id = 1;
        when(themeProvider.get(id)).thenReturn(darkTheme);
        when(modelAssembler.toModel(darkTheme)).thenReturn(darkThemeResponse);

        // Act
        ResponseEntity<ThemeResponse> response = themeController.get(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(darkThemeResponse, response.getBody());
        verify(themeProvider, times(1)).get(id);
        verify(modelAssembler, times(1)).toModel(darkTheme);
    }

    @Test
    void getAll_returnsAllThemes() {
        // Arrange
        when(themeProvider.getAll()).thenReturn(themeList);
        when(modelAssembler.toCollectionModel(themeList)).thenReturn(CollectionModel.of(themeResponseCollection));

        // Act
        ResponseEntity<Collection<ThemeResponse>> response = themeController.getAll();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().containsAll(themeResponseCollection));
        verify(themeProvider, times(1)).getAll();
        verify(modelAssembler, times(1)).toCollectionModel(themeList);
    }

}
