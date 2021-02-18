package org.example.delivery;

import static java.net.URLEncoder.encode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * AbstractTestcontainers class.
 *
 * @author hugo
 */
@Testcontainers
public abstract class AbstractTestcontainers {
  private static final String KEYCLOAK_SERVICE_NAME = "keycloak_1";
  private static final int KEYCLOAK_SERVICE_PORT = 8080;
  private static final String KEYCLOAK_TOKEN_PATH =
      "/auth/realms/deliveryService/protocol/openid-connect/token";
  private static final String DATABASE_SERVICE_NAME = "db_1";
  private static final int DATABASE_SERVICE_PORT = 5432;

  @Container
  private static final DockerComposeContainer<?> dockerComposeContainer =
      new DockerComposeContainer<>(new File("docker-compose.yml"))
          .withExposedService(KEYCLOAK_SERVICE_NAME, KEYCLOAK_SERVICE_PORT)
          .withExposedService(DATABASE_SERVICE_NAME, DATABASE_SERVICE_PORT)
          .withLocalCompose(true);
  public static final String KEYCLOAK_GRANT_TYPE = "grant_type";
  public static final String KEYCLOAK_CLIENT_ID = "client_id";
  public static final String KEYCLOAK_CLIENT_SECRET = "client_secret";

  @DynamicPropertySource
  static void dynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.security.oauth2.resourceserver.jwt.jwt-set-uri",
        () -> "http://"
            + dockerComposeContainer.getServiceHost(KEYCLOAK_SERVICE_NAME, KEYCLOAK_SERVICE_PORT)
            + ":"
            + dockerComposeContainer.getServicePort(KEYCLOAK_SERVICE_NAME, KEYCLOAK_SERVICE_PORT)
            + "/auth/realms/deliveryService/protocol/openid-connect/certs"
    );

    registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
        () -> "http://"
            + dockerComposeContainer.getServiceHost(KEYCLOAK_SERVICE_NAME, KEYCLOAK_SERVICE_PORT)
            + ":"
            + dockerComposeContainer.getServicePort(KEYCLOAK_SERVICE_NAME, KEYCLOAK_SERVICE_PORT)
            + "/auth/realms/deliveryService"
    );
  }

  @Autowired
  public transient TestRestTemplate testRestTemplate;

  @Test
  void containerShouldBeRunning() {
    assertTrue(dockerComposeContainer.getContainerByServiceName(DATABASE_SERVICE_NAME).isPresent());
    assertTrue(dockerComposeContainer.getContainerByServiceName(KEYCLOAK_SERVICE_NAME).isPresent());
  }

  /**
   * Get access token from keycloak.
   *
   * @param clientId     client identifier
   * @param clientSecret client secret
   * @param grantType    grant type
   * @return access token to be used to access the api
   * @throws JSONException when json is invalid
   */
  public String getKeycloakAccessToken(String clientId, String clientSecret, String grantType)
      throws JSONException {
    String body = Map.of(
        KEYCLOAK_GRANT_TYPE, grantType,
        KEYCLOAK_CLIENT_ID, clientId,
        KEYCLOAK_CLIENT_SECRET, clientSecret)
        .entrySet().stream().map(entry -> String.join("=",
            encode(entry.getKey(), StandardCharsets.UTF_8),
            encode(entry.getValue(), StandardCharsets.UTF_8))
        ).collect(Collectors.joining("&"));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    ResponseEntity<String> result = testRestTemplate.postForEntity(
        URI.create("http://"
            + dockerComposeContainer.getServiceHost(KEYCLOAK_SERVICE_NAME, KEYCLOAK_SERVICE_PORT)
            + ":"
            + dockerComposeContainer.getServicePort(KEYCLOAK_SERVICE_NAME, KEYCLOAK_SERVICE_PORT)
            + KEYCLOAK_TOKEN_PATH),
        new HttpEntity<>(body, headers),
        String.class
    );

    assertEquals(HttpStatus.OK, result.getStatusCode());

    JSONObject responseJson = new JSONObject(result.getBody());

    assertEquals("Bearer", responseJson.get("token_type"));
    assertNotNull(responseJson.get("access_token"));

    return responseJson.get("access_token").toString();
  }
}
