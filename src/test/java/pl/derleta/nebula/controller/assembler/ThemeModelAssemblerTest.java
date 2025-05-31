package pl.derleta.nebula.controller.assembler;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.hateoas.Link;
import pl.derleta.nebula.controller.ThemeController;
import pl.derleta.nebula.controller.mapper.ThemeApiMapper;
import pl.derleta.nebula.controller.response.ThemeResponse;
import pl.derleta.nebula.domain.model.Theme;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

class ThemeModelAssemblerTest {

    @Test
    void toModel_validNationality_addsSelfLinkToResponse() {
        // Arrange
        ThemeModelAssembler assembler = new ThemeModelAssembler();
        Theme entity = new Theme(1, "Dark");
        ThemeResponse mappedResponse = ThemeResponse.builder()
                .id(entity.id())
                .name(entity.name())
                .build();

        try (MockedStatic<ThemeApiMapper> mockedStatic = Mockito.mockStatic(ThemeApiMapper.class)) {
            mockedStatic.when(() -> ThemeApiMapper.toResponse(entity)).thenReturn(mappedResponse);

            Link expectedLink = linkTo(ThemeController.class)
                    .slash(ThemeController.DEFAULT_PATH)
                    .slash(mappedResponse.getId())
                    .withSelfRel();

            // Act
            ThemeResponse result = assembler.toModel(entity);

            // Assert
            assertThat(result.getId()).isEqualTo(entity.id());
            assertThat(result.getName()).isEqualTo(entity.name());
            assertThat(result.getLinks()).containsExactly(expectedLink);

            // Verify static method
            mockedStatic.verify(() -> ThemeApiMapper.toResponse(entity), times(1));
        }
    }

    @Test
    void toModel_validNationality_mapsFieldsCorrectly() {
        // Arrange
        ThemeModelAssembler assembler = new ThemeModelAssembler();
        Theme entity = new Theme(1, "Dark");
        ThemeResponse mappedResponse = ThemeResponse.builder()
                .id(entity.id())
                .name(entity.name())
                .build();

        try (MockedStatic<ThemeApiMapper> mockedStatic = Mockito.mockStatic(ThemeApiMapper.class)) {
            mockedStatic.when(() -> ThemeApiMapper.toResponse(entity)).thenReturn(mappedResponse);

            // Act
            ThemeResponse result = assembler.toModel(entity);

            // Assert
            assertThat(result.getId()).isEqualTo(entity.id());
            assertThat(result.getName()).isEqualTo(entity.name());
        }
    }

}
