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
import pl.derleta.nebula.controller.assembler.GenderModelAssembler;
import pl.derleta.nebula.controller.response.GenderResponse;
import pl.derleta.nebula.domain.model.Gender;
import pl.derleta.nebula.service.GenderProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenderControllerTest {

    @Mock
    private GenderProvider genderProvider;

    @Mock
    private GenderModelAssembler modelAssembler;

    @InjectMocks
    private GenderController genderController;

    private Gender male;
    private Gender female;
    private GenderResponse maleResponse;
    private GenderResponse femaleResponse;
    private List<Gender> genderList;
    private Collection<GenderResponse> genderResponseCollection;

    @BeforeEach
    void setUp() {
        // Set up test data
        male = new Gender(1, "Male");
        female = new Gender(2, "Female");
        genderList = Arrays.asList(male, female);

        maleResponse = GenderResponse.builder()
                .id(male.id())
                .name(male.name())
                .build();

        femaleResponse = GenderResponse.builder()
                .id(female.id())
                .name(female.name())
                .build();

        genderResponseCollection = Arrays.asList(maleResponse, femaleResponse);
    }

    @Test
    void get_validId_returnsGenderResponse() {
        // Arrange
        Integer id = 1;
        when(genderProvider.get(id)).thenReturn(male);
        when(modelAssembler.toModel(male)).thenReturn(maleResponse);

        // Act
        ResponseEntity<GenderResponse> response = genderController.get(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(maleResponse, response.getBody());
        verify(genderProvider, times(1)).get(id);
        verify(modelAssembler, times(1)).toModel(male);
    }

    @Test
    void getAll_returnsAllGenders() {
        // Arrange
        when(genderProvider.getAll()).thenReturn(genderList);
        when(modelAssembler.toCollectionModel(genderList)).thenReturn(CollectionModel.of(genderResponseCollection));

        // Act
        ResponseEntity<Collection<GenderResponse>> response = genderController.getAll();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().containsAll(genderResponseCollection));
        verify(genderProvider, times(1)).getAll();
        verify(modelAssembler, times(1)).toCollectionModel(genderList);
    }

}
