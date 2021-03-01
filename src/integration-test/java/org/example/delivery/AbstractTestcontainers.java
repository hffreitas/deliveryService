package org.example.delivery;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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
          .withExposedService(KEYCLOAK_SERVICE_NAME, KEYCLOAK_SERVICE_PORT, Wait.defaultWaitStrategy())
          .withExposedService(DATABASE_SERVICE_NAME, DATABASE_SERVICE_PORT, Wait.defaultWaitStrategy())
          .withLocalCompose(true);

  public static final String KEYCLOAK_GRANT_TYPE = "grant_type";
  public static final String KEYCLOAK_CLIENT_ID = "client_id";
  public static final String KEYCLOAK_CLIENT_SECRET = "client_secret";

  public static final String DELIVERY_SERVICE_APP_ADMIN_CLIENT_ID = "deliveryServiceAppAdmin";
  public static final String DELIVERY_SERVICE_APP_ADMIN_CLIENT_PASSWORD = "deliveryServiceAppAdmin";
  public static final String DELIVERY_SERVICE_APP_CLIENT_ID = "deliveryServiceApp";
  public static final String DELIVERY_SERVICE_APP_CLIENT_PASSWORD = "deliveryServiceApp";
  private static final String ACCESS_TOKEN_KEYWORD = "access_token";

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
   */
  public String getKeycloakAccessToken(String clientId, String clientSecret, String grantType) {
    return given().urlEncodingEnabled(true)
        .param(KEYCLOAK_GRANT_TYPE, grantType)
        .param(KEYCLOAK_CLIENT_ID, clientId)
        .param(KEYCLOAK_CLIENT_SECRET, clientSecret)
        .post("http://"
            + dockerComposeContainer.getServiceHost(KEYCLOAK_SERVICE_NAME, KEYCLOAK_SERVICE_PORT)
            + ":"
            + dockerComposeContainer.getServicePort(KEYCLOAK_SERVICE_NAME, KEYCLOAK_SERVICE_PORT)
            + KEYCLOAK_TOKEN_PATH)
        .then().statusCode(HttpStatus.OK.value())
        .extract().response().getBody().jsonPath().getString(ACCESS_TOKEN_KEYWORD);
  }
}
