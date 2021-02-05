package org.example.delivery.service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.delivery.entities.Route;
import org.example.delivery.exception.NoDataFoundException;
import org.example.delivery.model.RouteMap;
import org.example.delivery.model.WeightType;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.RouteRequest;
import org.example.delivery.payload.RouteResponse;
import org.example.delivery.payload.RouteUpdate;
import org.example.delivery.repository.PointRepository;
import org.example.delivery.repository.RouteRepository;
import org.example.delivery.util.DijkstraAlgorithm;
import org.example.delivery.util.PointMapper;
import org.example.delivery.util.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RouteServiceImpl class.
 *
 * @author hugo
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RouteServiceImpl implements RouteService {

  @Autowired
  private final RouteRepository routeRepository;

  @Autowired
  private final PointRepository pointRepository;

  @Override
  public Collection<RouteResponse> getAllRoutes() {
    return routeRepository.findAll().stream().map(RouteMapper::mapRouteToRouteResponse)
        .collect(Collectors.toList());
  }

  @Override
  public RouteResponse getRouteById(UUID id) {
    return routeRepository.findById(id)
        .map(RouteMapper::mapRouteToRouteResponse)
        .orElseThrow(NoDataFoundException::new);
  }

  @Override
  public RouteResponse createRoute(RouteRequest routeRequest) {

    Route route = new Route();
    route.setDestination(pointRepository.getOne(routeRequest.getDestination()));
    route.setOrigin(pointRepository.getOne(routeRequest.getOrigin()));
    route.setCost(routeRequest.getCost());
    route.setTime(routeRequest.getTime());

    return RouteMapper.mapRouteToRouteResponse(routeRepository.save(route));
  }

  @Override
  public Collection<PointResponse> getPath(UUID origin, UUID destination, WeightType type) {
    RouteMap routeMap = new RouteMap(routeRepository.findAll());
    DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(routeMap, type);

    dijkstraAlgorithm.execute(pointRepository.getOne(origin), pointRepository.getOne(destination));

    return dijkstraAlgorithm.getPath(pointRepository.getOne(destination))
        .orElseThrow(NoDataFoundException::new)
        .stream().map(PointMapper::mapPointToPointResponse)
        .collect(Collectors.toList());
  }

  @Override
  public RouteResponse updateRoute(UUID uuid, RouteUpdate routeUpdate) {
    Route route = routeRepository.findById(uuid).orElseThrow(NoDataFoundException::new);
    route.setTime(routeUpdate.getTime());
    route.setCost(routeUpdate.getCost());

    routeRepository.save(route);
    return RouteMapper.mapRouteToRouteResponse(route);
  }

  @Override
  public void deleteRoute(UUID uuid) {
    routeRepository.deleteById(uuid);
  }
}
