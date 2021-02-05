package org.example.delivery.util;

import org.example.delivery.entities.Route;
import org.example.delivery.payload.RouteResponse;

/**
 * RouteMapper class.
 *
 * @author hugo
 */
public class RouteMapper {

  private RouteMapper() {
    throw new IllegalStateException("RouteMapper class");
  }

  /**
   * maps route to routeResponse.
   *
   * @param route route object
   * @return routeResponse object
   */
  public static RouteResponse mapRouteToRouteResponse(Route route) {
    RouteResponse routeResponse = new RouteResponse();
    routeResponse.setId(route.getId());
    routeResponse.setCost(route.getCost());
    routeResponse.setTime(route.getTime());
    routeResponse.setOrigin(PointMapper.mapPointToPointBasicInfoResponse(route.getOrigin()));
    routeResponse
        .setDestination(PointMapper.mapPointToPointBasicInfoResponse(route.getDestination()));
    return routeResponse;
  }
}
