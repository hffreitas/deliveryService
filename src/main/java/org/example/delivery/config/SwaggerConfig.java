package org.example.delivery.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig class.
 *
 * @author hugo
 */
@Configuration
public class SwaggerConfig {
  public static final String BEARER_KEY_SECURITY_SCHEME = "bearer-key";

  @Value("${spring.application.name}")
  private transient String applicationName;

  /**
   * Customize openApi.
   *
   * @return OpenAPI
   */
  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
        .components(new Components().addSecuritySchemes(BEARER_KEY_SECURITY_SCHEME,
            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                .bearerFormat("JWT")))
        .info(new Info().title(applicationName));
  }

  @Bean
  public GroupedOpenApi customApi() {
    return GroupedOpenApi.builder().group("api").pathsToMatch("/v*/**").build();
  }

  @Bean
  public GroupedOpenApi actuatorApi() {
    return GroupedOpenApi.builder().group("actuator").pathsToMatch("/actuator/**").build();
  }
}
