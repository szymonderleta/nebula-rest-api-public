package pl.derleta.nebula.controller.assembler;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.hateoas.Link;
import pl.derleta.nebula.controller.GenderController;
import pl.derleta.nebula.controller.mapper.GenderApiMapper;
import pl.derleta.nebula.controller.response.GenderResponse;
import pl.derleta.nebula.domain.model.Gender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

class GenderModelAssemblerTest {

    @Test
    void toModel_validGender_addsSelfLinkToResponse() {
        // Arrange
        GenderModelAssembler assembler = new GenderModelAssembler();
        Gender entity = new Gender(1, "Male");
        GenderResponse mappedResponse = GenderResponse.builder()
                .id(entity.id())
                .name(entity.name())
                .build();

        // Mocking static method GenderApiMapper.toResponse()
        try (MockedStatic<GenderApiMapper> mockedStatic = Mockito.mockStatic(GenderApiMapper.class)) {
            mockedStatic.when(() -> GenderApiMapper.toResponse(entity)).thenReturn(mappedResponse);

            Link expectedLink = linkTo(GenderController.class)
                    .slash(GenderController.DEFAULT_PATH)
                    .slash(mappedResponse.getId())
                    .withSelfRel();

            // Act
            GenderResponse result = assembler.toModel(entity);

            // Assert
            assertThat(result.getId()).isEqualTo(entity.id());
            assertThat(result.getName()).isEqualTo(entity.name());
            assertThat(result.getLinks()).containsExactly(expectedLink);

            // Verify static method
            mockedStatic.verify(() -> GenderApiMapper.toResponse(entity), times(1));
        }
    }

    @Test
    void toModel_validGender_mapsFieldsCorrectly() {
        // Arrange
        GenderModelAssembler assembler = new GenderModelAssembler();
        Gender entity = new Gender(2, "Female");
        GenderResponse mappedResponse = GenderResponse.builder()
                .id(entity.id())
                .name(entity.name())
                .build();

        try (MockedStatic<GenderApiMapper> mockedStatic = Mockito.mockStatic(GenderApiMapper.class)) {
            mockedStatic.when(() -> GenderApiMapper.toResponse(entity)).thenReturn(mappedResponse);

            // Act
            GenderResponse result = assembler.toModel(entity);

            // Assert
            assertThat(result.getId()).isEqualTo(entity.id());
            assertThat(result.getName()).isEqualTo(entity.name());
        }
    }

}
