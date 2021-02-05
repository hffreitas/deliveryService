package org.example.delivery.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.example.delivery.entities.Point;
import org.example.delivery.entities.Route;
import org.example.delivery.exception.NoDataFoundException;
import org.example.delivery.model.WeightType;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.RouteRequest;
import org.example.delivery.payload.RouteResponse;
import org.example.delivery.payload.RouteUpdate;
import org.example.delivery.repository.PointRepository;
import org.example.delivery.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RouteServiceImplTest {

  private static final String idA = "411a0a56-5c8f-49bc-b0d7-4bfee67014c0";
  @Mock
  private RouteRepository routeRepository;
  @Mock
  private PointRepository pointRepository;
  @InjectMocks
  private RouteServiceImpl routeService;

  private final UUID uuid = UUID.fromString("c85fbf21-46f4-4c41-9b7c-670949ccec52");
  private Route route;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    Point point = new Point();
    point.setName("name");
    route = new Route();
    route.setTime(Integer.MAX_VALUE);
    route.setCost(Integer.MAX_VALUE);
    route.setUpdatedBy(Long.MAX_VALUE);
    route.setUpdatedAt(Instant.MAX);
    route.setCreatedAt(Instant.MAX);
    route.setCreatedBy(Long.MAX_VALUE);
    route.setOrigin(point);
    route.setDestination(point);
    route.setId(uuid);
  }

  @Test
  void testGetAllRoutes() {
    List<Route> routes = Collections.singletonList(route);
    when(routeRepository.findAll()).thenReturn(routes);

    Collection<RouteResponse> result = routeService.getAllRoutes();

    assertEquals(routes.size(), result.size());
  }

  @Test
  void testGetRoute() {
    when(routeRepository.findById(uuid)).thenReturn(Optional.of(route));

    RouteResponse result = routeService.getRouteById(uuid);

    assertEquals(route.getCost(), result.getCost());
    assertEquals(route.getTime(), result.getTime());

  }

  @Test
  void testGetRouteThrowsNoDataFound() {
    when(routeRepository.findById(uuid)).thenReturn(Optional.empty());

    assertThrows(NoDataFoundException.class, () -> routeService.getRouteById(uuid));
  }

  @Test
  void testCreateRoute() {
    RouteRequest routeRequest = new RouteRequest();
    routeRequest.setCost(Integer.MAX_VALUE);
    routeRequest.setDestination(uuid);
    routeRequest.setOrigin(uuid);
    routeRequest.setTime(Integer.MAX_VALUE);
    when(routeRepository.save(any())).thenReturn(route);
    when(pointRepository.getOne(uuid)).thenReturn(new Point());

    RouteResponse result = routeService.createRoute(routeRequest);

    assertEquals(route.getId(), result.getId());
    assertEquals(route.getCost(), result.getCost());
    assertEquals(route.getTime(), result.getTime());
  }

  @Test
  void testUpdateRoute() {
    RouteUpdate routeUpdate = new RouteUpdate();
    routeUpdate.setCost(Integer.MAX_VALUE);
    routeUpdate.setTime(Integer.MAX_VALUE);
    when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));
    when(routeRepository.save(any())).thenReturn(route);

    RouteResponse result = routeService.updateRoute(route.getId(), routeUpdate);

    assertEquals(route.getCost(), result.getCost());
    assertEquals(route.getTime(), result.getTime());
    assertEquals(route.getId(), result.getId());

  }

  @Test
  void testUpdateRouteThrowsNoDataFoundException() {
    RouteUpdate routeUpdate = new RouteUpdate();
    routeUpdate.setCost(Integer.MAX_VALUE);
    routeUpdate.setTime(Integer.MAX_VALUE);
    when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

    assertThrows(NoDataFoundException.class,
        () -> routeService.updateRoute(uuid, routeUpdate));
  }

  @Test
  void testDeleteRoute() {
    routeService.deleteRoute(route.getId());

    verify(routeRepository, times(1)).deleteById(route.getId());
  }

  @Test
  void testGetPathThrowsNoDataFoundException() {
    when(pointRepository.findAll()).thenReturn(new ArrayList<>());
    when(routeRepository.findAll()).thenReturn(new ArrayList<>());
    when(pointRepository.getOne(any())).thenReturn(new Point());

    assertThrows(NoDataFoundException.class,
        () -> routeService.getPath(uuid, uuid, WeightType.COST));
  }

  @Test
  void testGetPath() {
    Point pointA = new Point();
    pointA.setId(UUID.fromString("c85fbf21-46f4-4c41-9b7c-670949ccec01"));
    pointA.setName("A");
    Point pointB = new Point();
    pointB.setId(UUID.fromString("c85fbf21-46f4-4c41-9b7c-670949ccec02"));
    pointB.setName("B");
    Point pointC = new Point();
    pointC.setId(UUID.fromString("c85fbf21-46f4-4c41-9b7c-670949ccec03"));
    pointC.setName("C");

    Route routeAtoB = new Route();
    routeAtoB.setOrigin(pointA);
    routeAtoB.setDestination(pointB);
    routeAtoB.setCost(1);
    routeAtoB.setTime(2);
    Route routeBtoC = new Route();
    routeBtoC.setOrigin(pointB);
    routeBtoC.setDestination(pointC);
    routeBtoC.setTime(4);
    routeBtoC.setCost(3);
    List<Route> routes = Arrays.asList(routeAtoB, routeBtoC);
    List<Point> points = Arrays.asList(pointA, pointB, pointC);
    when(pointRepository.findAll()).thenReturn(points);
    when(routeRepository.findAll()).thenReturn(routes);
    when(pointRepository.getOne(pointC.getId())).thenReturn(pointC);
    when(pointRepository.getOne(pointA.getId())).thenReturn(pointA);

    Collection<PointResponse> result =
        routeService.getPath(pointA.getId(), pointC.getId(), WeightType.COST);

    assertEquals(3, result.size());
  }

  @Test
  void testGetPathComplexAtoBbyTime() {
    UUID origin = UUID.fromString(idA);
    UUID destination = UUID.fromString("411a0a56-5c8f-49bc-b0d7-4bfee67014c1");
    getPath(origin, destination);

    Collection<PointResponse> path = routeService.getPath(origin, destination, WeightType.TIME);

    assertEquals(3, path.size());
  }

  @Test
  void testGetPathComplexAtoBbyCost() {
    UUID origin = UUID.fromString(idA);
    UUID destination = UUID.fromString("411a0a56-5c8f-49bc-b0d7-4bfee67014c1");
    getPath(origin, destination);

    Collection<PointResponse> path = routeService.getPath(origin, destination, WeightType.COST);

    assertEquals(3, path.size());
  }

  @Test
  void testGetPathComplexAtoDbyCost() {
    UUID origin = UUID.fromString(idA);
    UUID destination = UUID.fromString("411a0a56-5c8f-49bc-b0d7-4bfee67014c3");
    getPath(origin, destination);

    Collection<PointResponse> path = routeService.getPath(origin, destination, WeightType.COST);

    assertEquals(4, path.size());
  }

  @Test
  void testGetPathComplexAtoDbyTime() {
    UUID origin = UUID.fromString(idA);
    UUID destination = UUID.fromString("411a0a56-5c8f-49bc-b0d7-4bfee67014c3");
    getPath(origin, destination);

    Collection<PointResponse> path = routeService.getPath(origin, destination, WeightType.TIME);

    assertEquals(3, path.size());
  }

  @Test
  void testGetPathComplexAtoC_ThrowsNoDataFound() {
    UUID origin = UUID.fromString(idA);
    UUID destination = UUID.fromString("411a0a56-5c8f-49bc-b0d7-4bfee67014c2");
    getPath(origin, destination);

    assertThrows(NoDataFoundException.class,
        () -> routeService.getPath(origin, destination, WeightType.COST));
  }

  private void getPath(UUID origin, UUID destination) {
    Point pointA = createPoint("A", 0);
    Point pointB = createPoint("B", 1);
    Point pointC = createPoint("C", 2);
    Point pointD = createPoint("D", 3);
    Point pointE = createPoint("E", 4);
    Point pointF = createPoint("F", 5);
    Point pointG = createPoint("G", 6);
    Point pointH = createPoint("H", 7);
    Point pointI = createPoint("I", 8);

    List<Point> points =
        Arrays.asList(pointA, pointB, pointC, pointD, pointE, pointF, pointG, pointH, pointI);
    List<Route> routes = Arrays.asList(
        createRoute(pointA, pointC, 20, 1),
        createRoute(pointA, pointE, 5, 30),
        createRoute(pointA, pointH, 1, 10),
        createRoute(pointC, pointB, 12, 1),
        createRoute(pointD, pointF, 50, 4),
        createRoute(pointE, pointD, 5, 3),
        createRoute(pointF, pointG, 50, 40),
        createRoute(pointF, pointI, 50, 45),
        createRoute(pointG, pointB, 73, 64),
        createRoute(pointH, pointE, 1, 30),
        createRoute(pointI, pointB, 5, 65)
    );

    when(pointRepository.findAll()).thenReturn(points);
    when(routeRepository.findAll()).thenReturn(routes);
    when(pointRepository.getOne(origin))
        .thenReturn(points.stream().filter(p -> p.getId().equals(origin)).findFirst().get());
    when(pointRepository.getOne(destination))
        .thenReturn(points.stream().filter(p -> p.getId().equals(destination)).findFirst().get());
  }

  private Point createPoint(String name, int i) {
    Point point = new Point();
    point.setId(UUID.fromString("411a0a56-5c8f-49bc-b0d7-4bfee67014c" + i));
    point.setName(name);
    return point;
  }

  private Route createRoute(Point origin, Point destination, Integer cost, Integer time) {
    Route route = new Route();
    route.setOrigin(origin);
    route.setDestination(destination);
    route.setTime(time);
    route.setCost(cost);

    return route;
  }
}