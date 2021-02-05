package org.example.delivery.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.example.delivery.model.WeightType;
import org.example.delivery.payload.PointBasicInfoResponse;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.RouteRequest;
import org.example.delivery.payload.RouteResponse;
import org.example.delivery.payload.RouteUpdate;
import org.example.delivery.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RouteControllerTest {

  @Mock
  private RouteService routeService;
  @InjectMocks
  private RouteController routeController;

  private RouteResponse routeResponse;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    routeResponse = new RouteResponse();
    routeResponse.setTime(Integer.MAX_VALUE);
    routeResponse.setCost(Integer.MAX_VALUE);
    routeResponse.setOrigin(new PointBasicInfoResponse());
    routeResponse.setDestination(new PointBasicInfoResponse());
    routeResponse.setId(UUID.randomUUID());
  }

  @Test
  void testGetAllRoutes() {
    Collection<RouteResponse> routes = Collections.singletonList(routeResponse);
    when(routeService.getAllRoutes()).thenReturn(routes);

    Collection<RouteResponse> result = routeController.getAllRoutes();

    assertEquals(1, result.size());
  }

  @Test
  void testGetRoute() {
    when(routeService.getRouteById(routeResponse.getId())).thenReturn(routeResponse);

    RouteResponse result = routeController.getRouteById(routeResponse.getId());

    assertEquals(routeResponse.getCost(), result.getCost());
    assertEquals(routeResponse.getTime(), result.getTime());
    assertEquals(routeResponse.getDestination(), result.getDestination());
    assertEquals(routeResponse.getOrigin(), result.getOrigin());
    assertEquals(routeResponse.getId(), result.getId());
  }

  @Test
  void testCreateRoute() {
    RouteRequest routeRequest = new RouteRequest();
    routeRequest.setTime(Integer.MAX_VALUE);
    routeRequest.setCost(Integer.MAX_VALUE);
    routeRequest.setOrigin(routeResponse.getOrigin().getId());
    routeRequest.setDestination(routeResponse.getDestination().getId());

    when(routeService.createRoute(routeRequest)).thenReturn(routeResponse);

    RouteResponse result = routeController.createRoute(routeRequest);

    assertEquals(routeResponse.getId(), result.getId());
    assertEquals(routeResponse.getDestination(), result.getDestination());
    assertEquals(routeResponse.getOrigin(), result.getOrigin());
    assertEquals(routeResponse.getTime(), result.getTime());
    assertEquals(routeResponse.getCost(), result.getCost());
  }

  @Test
  void testUpdateRoute() {
    RouteUpdate routeUpdate = new RouteUpdate();
    routeUpdate.setTime(Integer.MAX_VALUE);
    routeUpdate.setCost(Integer.MAX_VALUE);
    when(routeService.updateRoute(routeResponse.getId(), routeUpdate)).thenReturn(routeResponse);

    RouteResponse result = routeController.updateRoute(routeResponse.getId(), routeUpdate);

    assertEquals(routeResponse.getCost(), result.getCost());
    assertEquals(routeResponse.getTime(), result.getTime());
    assertEquals(routeResponse.getId(), result.getId());
    assertEquals(routeResponse.getOrigin(), result.getOrigin());
    assertEquals(routeResponse.getDestination(), result.getDestination());
  }

  @Test
  void testDeleteRoute() {
    routeController.deleteRoute(routeResponse.getId());

    verify(routeService, times(1)).deleteRoute(routeResponse.getId());
  }

  @Test
  void testGetPath() {
    PointResponse pointResponse = new PointResponse();
    Collection<PointResponse> path = Collections.singletonList(pointResponse);
    when(routeService.getPath(any(), any(), any())).thenReturn(path);

    Collection<PointResponse> result =
        routeController.getPath(UUID.randomUUID(), UUID.randomUUID(), WeightType.COST);

    assertEquals(path.size(), result.size());
  }

}