package pl.derleta.nebula.controller.assembler;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.hateoas.Link;
import pl.derleta.nebula.controller.NationalityController;
import pl.derleta.nebula.controller.mapper.NationalityApiMapper;
import pl.derleta.nebula.controller.mapper.RegionApiMapper;
import pl.derleta.nebula.controller.response.NationalityResponse;
import pl.derleta.nebula.domain.model.Nationality;
import pl.derleta.nebula.domain.model.Region;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

class NationalityModelAssemblerTest {

    @Test
    void toModel_validNationality_addsSelfLinkToResponse() {
        // Arrange
        NationalityModelAssembler assembler = new NationalityModelAssembler();
        Nationality entity = new Nationality(1, "Polish", "PL", new Region(1, "Europe"));
        NationalityResponse mappedResponse = NationalityResponse.builder()
                .id(entity.id())
                .name(entity.name())
                .region(RegionApiMapper.toResponse(entity.region()))
                .build();

        try (MockedStatic<NationalityApiMapper> mockedStatic = Mockito.mockStatic(NationalityApiMapper.class)) {
            mockedStatic.when(() -> NationalityApiMapper.toResponse(entity)).thenReturn(mappedResponse);

            Link expectedLink = linkTo(NationalityController.class)
                    .slash(NationalityController.DEFAULT_PATH)
                    .slash(mappedResponse.getId())
                    .withSelfRel();

            // Act
            NationalityResponse result = assembler.toModel(entity);

            // Assert
            assertThat(result.getId()).isEqualTo(entity.id());
            assertThat(result.getName()).isEqualTo(entity.name());

            assertThat(result.getLinks()).containsExactly(expectedLink);

            // Verify static method
            mockedStatic.verify(() -> NationalityApiMapper.toResponse(entity), times(1));
        }
    }

    @Test
    void toModel_validNationality_mapsFieldsCorrectly() {
        // Arrange
        NationalityModelAssembler assembler = new NationalityModelAssembler();
        Nationality entity = new Nationality(1, "Polish", "PL", new Region(1, "Europe"));
        NationalityResponse mappedResponse = NationalityResponse.builder()
                .id(entity.id())
                .name(entity.name())
                .region(RegionApiMapper.toResponse(entity.region()))
                .build();

        try (MockedStatic<NationalityApiMapper> mockedStatic = Mockito.mockStatic(NationalityApiMapper.class)) {
            mockedStatic.when(() -> NationalityApiMapper.toResponse(entity)).thenReturn(mappedResponse);

            // Act
            NationalityResponse result = assembler.toModel(entity);

            // Assert
            assertThat(result.getId()).isEqualTo(entity.id());
            assertThat(result.getName()).isEqualTo(entity.name());
            assertThat(result.getRegion().getId()).isEqualTo(entity.region().id());
            assertThat(result.getRegion().getName()).isEqualTo(entity.region().name());
        }
    }

}
