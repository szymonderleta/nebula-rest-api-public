package pl.derleta.nebula.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(AppConfig.class)
public class AppConfigTest {

    private AppConfig appConfig = new AppConfig();

    @Test
    public void corsConfigurationSource_shouldNotBeNull() {
        CorsConfigurationSource corsConfigurationSource = appConfig.corsConfigurationSource();
        assertThat(corsConfigurationSource).isNotNull();
    }

    @Test
    public void restTemplate_shouldNotBeNull() {
        RestTemplate restTemplate = appConfig.restTemplate();
        assertThat(restTemplate).isNotNull();
    }

}
