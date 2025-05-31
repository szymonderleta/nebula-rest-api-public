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
import pl.derleta.nebula.controller.assembler.NationalityModelAssembler;
import pl.derleta.nebula.controller.response.NationalityResponse;
import pl.derleta.nebula.controller.response.RegionResponse;
import pl.derleta.nebula.domain.model.Nationality;
import pl.derleta.nebula.domain.model.Region;
import pl.derleta.nebula.service.NationalityProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NationalityControllerTest {

    @Mock
    private NationalityProvider nationalityProvider;

    @Mock
    private NationalityModelAssembler modelAssembler;

    @InjectMocks
    private NationalityController nationalityController;

    private Region europeRegion;
    private Region asiaRegion;
    private Nationality polishNationality;
    private Nationality japaneseNationality;
    private RegionResponse europeRegionResponse;
    private RegionResponse asiaRegionResponse;
    private NationalityResponse polishNationalityResponse;
    private NationalityResponse japaneseNationalityResponse;
    private List<Nationality> nationalityList;
    private Collection<NationalityResponse> nationalityResponseCollection;

    @BeforeEach
    void setUp() {
        // Set up test data
        europeRegion = new Region(1, "Europe");
        asiaRegion = new Region(2, "Asia");

        polishNationality = new Nationality(1, "Polish", "PL", europeRegion);
        japaneseNationality = new Nationality(2, "Japanese", "JP", asiaRegion);

        nationalityList = Arrays.asList(polishNationality, japaneseNationality);

        europeRegionResponse = RegionResponse.builder()
                .id(europeRegion.id())
                .name(europeRegion.name())
                .build();

        asiaRegionResponse = RegionResponse.builder()
                .id(asiaRegion.id())
                .name(asiaRegion.name())
                .build();

        polishNationalityResponse = NationalityResponse.builder()
                .id(polishNationality.id())
                .name(polishNationality.name())
                .code(polishNationality.code())
                .region(europeRegionResponse)
                .build();

        japaneseNationalityResponse = NationalityResponse.builder()
                .id(japaneseNationality.id())
                .name(japaneseNationality.name())
                .code(japaneseNationality.code())
                .region(asiaRegionResponse)
                .build();

        nationalityResponseCollection = Arrays.asList(polishNationalityResponse, japaneseNationalityResponse);
    }

    @Test
    void get_validId_returnsNationalityResponse() {
        // Arrange
        Integer id = 1;
        when(nationalityProvider.get(id)).thenReturn(polishNationality);
        when(modelAssembler.toModel(polishNationality)).thenReturn(polishNationalityResponse);

        // Act
        ResponseEntity<NationalityResponse> response = nationalityController.get(id);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(polishNationalityResponse, response.getBody());
        verify(nationalityProvider, times(1)).get(id);
        verify(modelAssembler, times(1)).toModel(polishNationality);
    }

    @Test
    void getAll_returnsAllNationalities() {
        // Arrange
        when(nationalityProvider.getAll()).thenReturn(nationalityList);
        when(modelAssembler.toCollectionModel(nationalityList)).thenReturn(CollectionModel.of(nationalityResponseCollection));

        // Act
        ResponseEntity<Collection<NationalityResponse>> response = nationalityController.getAll();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().containsAll(nationalityResponseCollection));
        verify(nationalityProvider, times(1)).getAll();
        verify(modelAssembler, times(1)).toCollectionModel(nationalityList);
    }

}
