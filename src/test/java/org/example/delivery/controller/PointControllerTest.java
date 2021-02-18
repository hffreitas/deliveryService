package org.example.delivery.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.example.delivery.payload.PointRequest;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.PointUpdate;
import org.example.delivery.security.dto.DeliveryUserDetails;
import org.example.delivery.service.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * PointController test class.
 *
 * @author hugo
 */
class PointControllerTest {

  private final String name = "name";
  @Mock
  private PointService pointService;
  @InjectMocks
  private PointController pointController;

  private PointResponse pointResponse;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    pointResponse = new PointResponse();
    pointResponse.setId(UUID.randomUUID());
    pointResponse.setName(name);
    pointResponse.setRoutes(Collections.emptyList());
  }

  @Test
  void testGetAllPoints() {
    Collection<PointResponse> points = Collections.singletonList(pointResponse);
    when(pointService.getAllPoints()).thenReturn(points);

    Collection<PointResponse> result = pointController.getAllPoints(new DeliveryUserDetails());

    assertEquals(1, result.size());
  }

  @Test
  void testGetPoint() {
    when(pointService.getPoint(name)).thenReturn(pointResponse);

    PointResponse result = pointController.getPointByName(name);

    assertEquals(pointResponse.getName(), result.getName());
    assertEquals(pointResponse.getId(), result.getId());
    assertEquals(pointResponse.getRoutes(), result.getRoutes());
  }

  @Test
  void testCreatePoint() {
    PointRequest pointRequest = new PointRequest();
    pointRequest.setName(name);
    when(pointService.createPoint(pointRequest)).thenReturn(pointResponse);

    PointResponse result = pointController.createPoint(pointRequest);

    assertEquals(pointResponse.getName(), result.getName());
    assertEquals(pointResponse.getRoutes(), result.getRoutes());
    assertEquals(pointResponse.getId(), result.getId());
  }

  @Test
  void testUpdatePoint() {
    PointUpdate pointUpdate = new PointUpdate();
    pointUpdate.setName("otherName");
    when(pointService.updatePoint(pointResponse.getId(), pointUpdate)).thenReturn(pointResponse);

    PointResponse result = pointController.updatePoint(pointResponse.getId(), pointUpdate);

    assertEquals(pointResponse.getId(), result.getId());
    assertEquals(pointResponse.getName(), result.getName());
    assertEquals(pointResponse.getRoutes(), result.getRoutes());
  }

  @Test
  void testDeletePoint() {
    pointController.deletePoint(pointResponse.getId());

    verify(pointService, times(1)).deletePoint(pointResponse.getId());
  }
}