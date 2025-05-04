package pl.derleta.nebula.controller.assembler;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.hateoas.Link;
import pl.derleta.nebula.controller.TokenController;
import pl.derleta.nebula.controller.mapper.TokenDataApiMapper;
import pl.derleta.nebula.controller.response.TokenDataResponse;
import pl.derleta.nebula.domain.token.TokenData;


import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

class TokenModelAssemblerTest {

    @Test
    void toModel_validTokenData_addsSelfLinkToResponse() {
        // Arrange
        TokenModelAssembler assembler = new TokenModelAssembler();
        TokenData entity = new TokenData(true, 123L, "tester@example.com", "token-value", Set.of());
        TokenDataResponse mappedResponse = TokenDataResponse.builder()
                .valid(entity.isValid())
                .userId(entity.getUserId())
                .email(entity.getEmail())
                .token(entity.getToken())
                .roles(entity.getRoles())
                .build();

        try (MockedStatic<TokenDataApiMapper> mockedStatic = Mockito.mockStatic(TokenDataApiMapper.class)) {
            mockedStatic.when(() -> TokenDataApiMapper.toResponse(entity)).thenReturn(mappedResponse);

            Link expectedLink = linkTo(TokenController.class)
                    .slash(TokenController.DEFAULT_PATH)
                    .withSelfRel();

            // Act
            TokenDataResponse result = assembler.toModel(entity);

            // Assert
            assertThat(result.isValid()).isEqualTo(entity.isValid());
            assertThat(result.getUserId()).isEqualTo(entity.getUserId());
            assertThat(result.getToken()).isEqualTo(entity.getToken());
            assertThat(result.getLinks()).containsExactly(expectedLink);

            // Verify static method
            mockedStatic.verify(() -> TokenDataApiMapper.toResponse(entity), times(1));
        }
    }

    @Test
    void toModel_validTokenData_mapsFieldsCorrectly() {
        // Arrange
        TokenModelAssembler assembler = new TokenModelAssembler();
        TokenData entity = new TokenData(true, 123L, "tester@example.com", "token-value", Set.of());
        TokenDataResponse mappedResponse = TokenDataResponse.builder()
                .valid(entity.isValid())
                .userId(entity.getUserId())
                .email(entity.getEmail())
                .token(entity.getToken())
                .roles(entity.getRoles())
                .build();

        try (MockedStatic<TokenDataApiMapper> mockedStatic = Mockito.mockStatic(TokenDataApiMapper.class)) {
            mockedStatic.when(() -> TokenDataApiMapper.toResponse(entity)).thenReturn(mappedResponse);

            // Act
            TokenDataResponse result = assembler.toModel(entity);

            // Assert
            assertThat(result.isValid()).isEqualTo(entity.isValid());
            assertThat(result.getUserId()).isEqualTo(entity.getUserId());
            assertThat(result.getEmail()).isEqualTo(entity.getEmail());
            assertThat(result.getToken()).isEqualTo(entity.getToken());
            assertThat(result.getRoles().size()).isEqualTo(entity.getRoles().size());
        }
    }

}
