package pl.derleta.nebula.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration class for customizing the Spring Web MVC setup.
 * It is annotated with {@link Configuration} to indicate that it defines beans and
 * with {@link EnableHypermediaSupport} to enable support for Hypermedia Application Language (HAL) in the APIs.
 */
@Configuration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Configures CORS (Cross-Origin Resource Sharing) mappings for the application.
     * This method allows specific origins and HTTP methods to access the specified API paths.
     * Primarily configured for the test environment.
     *
     * @param registry the {@link CorsRegistry} used to add and configure CORS mappings.
     */
    // FIXME: only For Test Environment
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins("http://localhost:3000", "https://localhost:3000", "https://milkyway.local:8555")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * Configures resource handlers for serving static resources.
     * This method maps specific URL patterns to resource locations within the classpath.
     * It is used to serve Swagger UI and webjar resources.
     *
     * @param registry the {@link ResourceHandlerRegistry} used to register the resource handlers
     *                 and assign the corresponding resource locations.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Adds custom {@link HandlerMethodArgumentResolver} instances to the list of resolvers.
     * These resolvers are used to process controller method arguments for handling pagination,
     * sorting, and page resource assembling in hypermedia-driven APIs.
     *
     * @param argumentResolvers the list of {@link HandlerMethodArgumentResolver} instances to which
     *                          custom resolvers are added.
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(pageableResolver());
        argumentResolvers.add(sortResolver());
        argumentResolvers.add(pagedResourcesAssemblerArgumentResolver());
    }

    /**
     * Creates and returns a {@link HateoasSortHandlerMethodArgumentResolver} bean.
     * This resolver is used to handle and resolve sorting requests in Spring MVC controllers,
     * enabling support for sorting in hypermedia-driven APIs.
     *
     * @return an instance of {@link HateoasSortHandlerMethodArgumentResolver} to handle sorting parameters.
     */
    @Bean
    public HateoasSortHandlerMethodArgumentResolver sortResolver() {
        return new HateoasSortHandlerMethodArgumentResolver();
    }

    /**
     * Creates and returns a {@link HateoasPageableHandlerMethodArgumentResolver} bean.
     * This resolver is used to handle and resolve pageable requests in Spring MVC controllers,
     * enabling support for pagination in hypermedia-driven APIs.
     *
     * @return an instance of {@link HateoasPageableHandlerMethodArgumentResolver} that
     * is configured with {@link HateoasSortHandlerMethodArgumentResolver} for handling sorting.
     */
    @Bean
    public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
        return new HateoasPageableHandlerMethodArgumentResolver(sortResolver());
    }

    /**
     * Creates and returns a {@link PagedResourcesAssembler} bean to facilitate
     * the creation of paginated resources in response to paginated queries.
     * This assembler simplifies the process of creating pageable resource representations.
     *
     * @return an instance of {@link PagedResourcesAssembler} configured with
     * {@link HateoasPageableHandlerMethodArgumentResolver} for resolving pageable requests.
     */
    @Bean
    public PagedResourcesAssembler<?> pagedResourcesAssembler() {
        return new PagedResourcesAssembler<>(pageableResolver(), null);
    }

    /**
     * Creates and returns a {@link PagedResourcesAssemblerArgumentResolver} bean.
     * This resolver enables the handling of pageable and assembling resources
     * in paged formats for Spring MVC controllers.
     *
     * @return an instance of {@link PagedResourcesAssemblerArgumentResolver}, configured with
     * {@link HateoasPageableHandlerMethodArgumentResolver} for handling pagination.
     */
    @Bean
    public PagedResourcesAssemblerArgumentResolver pagedResourcesAssemblerArgumentResolver() {
        return new PagedResourcesAssemblerArgumentResolver(pageableResolver());
    }

}
