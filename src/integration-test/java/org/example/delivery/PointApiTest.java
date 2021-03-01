package org.example.delivery;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.restassured.http.ContentType;
import java.util.Optional;
import java.util.UUID;
import org.apache.http.HttpStatus;
import org.example.delivery.entities.Point;
import org.example.delivery.payload.PointRequest;
import org.example.delivery.payload.PointUpdate;
import org.example.delivery.repository.PointRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

/**
 * RandomPortTest class.
 *
 * @author hugo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PointApiTest extends AbstractTestcontainers {

  public static final String POINTS_PATH = "/v1/points/";
  public static final String ALL_POINTS_PATH = "/v1/points/all";
  public static final String GRANT_TYPE = "client_credentials";
  public static final String LOCALHOST = "http://localhost:";

  @Autowired
  private transient PointRepository pointRepository;
  @LocalServerPort
  private transient int port;

  @Test
  public void givenNoPointsWhenGetAllPointsReturnStatusOkAndEmptyList() {
    get(LOCALHOST + port + ALL_POINTS_PATH)
        .then().statusCode(HttpStatus.SC_OK)
        .assertThat().body("", hasSize(0));
  }

  @Test
  public void givenOnePointWhenGetAllPointsReturnStatusOkAndListWithOneElement() {
    Point mockPoint = mockPoint();
    pointRepository.save(mockPoint);

    get(LOCALHOST + port + ALL_POINTS_PATH)
        .then().statusCode(HttpStatus.SC_OK)
        .assertThat().body("$", hasSize(1));

    pointRepository.deleteById(mockPoint.getId());
  }

  @Test
  public void givenOnePointWhenGetPointByNameReturnStatusOkAndPointResponse() {
    Point mockPoint = mockPoint();
    pointRepository.save(mockPoint);

    get(LOCALHOST + port + POINTS_PATH + mockPoint.getName())
        .then().statusCode(HttpStatus.SC_OK)
        .contentType(ContentType.JSON)
        .assertThat().body("name", equalTo(mockPoint.getName()));

    pointRepository.deleteById(mockPoint.getId());
  }

  @Test
  public void givenAnNonExistentPointWhenGetPointByNameReturnNoDataFound() {
    get(LOCALHOST + port + POINTS_PATH + "pointB")
        .then().statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  public void givenValidPointWhenCreatePointAuthenticatedThenReturnStatusCreatedAndPointJson() {
    PointRequest pointRequest = new PointRequest();
    pointRequest.setName("test");
    String accessToken = getKeycloakAccessToken(
        DELIVERY_SERVICE_APP_ADMIN_CLIENT_ID,
        DELIVERY_SERVICE_APP_ADMIN_CLIENT_PASSWORD,
        GRANT_TYPE);

    String id = given().auth().oauth2(accessToken).contentType(ContentType.JSON)
        .body(pointRequest)
        .post(LOCALHOST + port + POINTS_PATH)
        .then().statusCode(HttpStatus.SC_OK)
        .assertThat().body("name", equalTo(pointRequest.getName()))
        .extract().path("id");

    Optional<Point> pointFromRepo = pointRepository.findById(UUID.fromString(id));
    assertTrue(pointFromRepo.isPresent());
    assertEquals(pointRequest.getName(), pointFromRepo.get().getName());

    pointRepository.deleteById(pointFromRepo.get().getId());
  }

  @Test
  public void givenValidPointWhenCreatePointAuthenticatedWithoutAdminRoleThenReturnStatusCreatedAndPointJson() {
    PointRequest pointRequest = new PointRequest();
    pointRequest.setName("test");
    String accessToken = getKeycloakAccessToken(
        DELIVERY_SERVICE_APP_CLIENT_ID,
        DELIVERY_SERVICE_APP_CLIENT_PASSWORD,
        GRANT_TYPE);

    given().auth().oauth2(accessToken).contentType(ContentType.JSON)
        .body(pointRequest)
        .post(LOCALHOST + port + POINTS_PATH)
        .then().statusCode(HttpStatus.SC_FORBIDDEN);
  }

  @Test
  public void givenAnExistingPointWhenUpdatePointAuthenticatedWithAdminRoleThenReturnStatusOkAndPointJson() {
    Point point = new Point();
    point.setName("pointA");
    pointRepository.save(point);

    PointUpdate pointUpdate = new PointUpdate();
    pointUpdate.setName("nameUpdated");

    String accessToken = getKeycloakAccessToken(
        DELIVERY_SERVICE_APP_ADMIN_CLIENT_ID,
        DELIVERY_SERVICE_APP_ADMIN_CLIENT_PASSWORD,
        GRANT_TYPE);

    String id = given().auth().oauth2(accessToken).contentType(ContentType.JSON)
        .body(pointUpdate)
        .patch(LOCALHOST + port + POINTS_PATH + point.getId())
        .then().statusCode(HttpStatus.SC_OK)
        .assertThat().body("name", equalTo(pointUpdate.getName()))
        .extract().path("id");

    Optional<Point> pointFromRepo = pointRepository.findById(UUID.fromString(id));
    assertTrue(pointFromRepo.isPresent());
    assertEquals(pointUpdate.getName(), pointFromRepo.get().getName());
    pointRepository.deleteById(pointFromRepo.get().getId());
  }


  private Point mockPoint() {
    Point point = new Point();
    point.setName("pointA");
    return point;
  }
}
