package org.example.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import org.example.delivery.entities.Point;
import org.example.delivery.payload.PointRequest;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.repository.PointRepository;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

/**
 * RandomPortTest class.
 *
 * @author hugo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RandomPortTest extends AbstractTestcontainers {

  @Autowired
  private transient PointRepository pointRepository;
  @LocalServerPort
  private transient int port;

  @Test
  public void givenNoPointsWhenGetAllPointsReturnStatusOkAndEmptyList() {
    ResponseEntity<ArrayList> responseEntity =
        testRestTemplate.getForEntity("/v1/points/all", ArrayList.class);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertThat(responseEntity.getBody()).isEmpty();
  }

  @Test
  public void givenOnePointWhenGetAllPointsReturnStatusOkAndListWithOneElement() {
    Point point = new Point();
    point.setName("pointA");
    pointRepository.save(point);

    ResponseEntity<ArrayList> responseEntity =
        testRestTemplate.getForEntity("/v1/points/all", ArrayList.class);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertFalse(responseEntity.getBody().isEmpty());
  }

  @Test
  public void givenValidPointWhenCreatePointAuthenticatedThenReturnStatusCreatedAndPointJson()
      throws JSONException {
    PointRequest pointRequest = new PointRequest();
    pointRequest.setName("test");
    String accessToken = getKeycloakAccessToken(
        "deliveryServiceApp",
        "deliveryServiceApp",
        "client_credentials");

    HttpHeaders headers = authBearerHeaders(accessToken);

    ResponseEntity<PointResponse> responseEntity = testRestTemplate.postForEntity(
        "http://localhost:" + port + "/v1/points/",
        new HttpEntity<>(pointRequest, headers),
        PointResponse.class
    );

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  private HttpHeaders authBearerHeaders(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    return headers;
  }

}
