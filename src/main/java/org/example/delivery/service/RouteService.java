package org.example.delivery.service;

import java.util.Collection;
import java.util.UUID;
import org.example.delivery.model.WeightType;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.RouteRequest;
import org.example.delivery.payload.RouteResponse;
import org.example.delivery.payload.RouteUpdate;

/**
 * RouteService interface.
 *
 * @author hugo
 */
public interface RouteService {
  Collection<RouteResponse> getAllRoutes();

  RouteResponse getRouteById(UUID id);

  RouteResponse createRoute(RouteRequest routeRequest);

  Collection<PointResponse> getPath(UUID origin, UUID destination, WeightType type);

  RouteResponse updateRoute(UUID uuid, RouteUpdate routeUpdate);

  void deleteRoute(UUID uuid);
}
