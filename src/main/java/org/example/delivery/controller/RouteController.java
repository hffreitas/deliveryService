package org.example.delivery.controller;

import static org.example.delivery.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.delivery.model.WeightType;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.RouteRequest;
import org.example.delivery.payload.RouteResponse;
import org.example.delivery.payload.RouteUpdate;
import org.example.delivery.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RouteController class.
 *
 * @author hugo
 */
@RestController
@RequestMapping("v1/routes")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RouteController {

  @Autowired
  private final RouteService routeService;

  @Operation(summary = "Get all routes")
  @GetMapping("/all")
  public Collection<RouteResponse> getAllRoutes() {
    return routeService.getAllRoutes();
  }

  @Operation(summary = "Get route by id")
  @GetMapping("/{id}")
  public RouteResponse getRouteById(@PathVariable("id") UUID id) {
    return routeService.getRouteById(id);
  }

  @Operation(summary = "Get path from two points")
  @GetMapping("/path/{origin}/{destination}/{type}")
  public Collection<PointResponse> getPath(
      @PathVariable("origin") UUID origin,
      @PathVariable("destination") UUID destination,
      @PathVariable("type") WeightType type) {
    return routeService.getPath(origin, destination, type);
  }

  @Operation(summary = "Create a route", security = {
      @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public RouteResponse createRoute(@RequestBody RouteRequest routeRequest) {
    return routeService.createRoute(routeRequest);
  }

  @Operation(summary = "Update a route", security = {
      @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public RouteResponse updateRoute(
      @PathVariable("id") UUID uuid,
      @RequestBody RouteUpdate routeUpdate) {
    return routeService.updateRoute(uuid, routeUpdate);
  }

  @Operation(summary = "Delete a route", security = {
      @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @DeleteMapping("{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public void deleteRoute(@PathVariable("id") UUID uuid) {
    routeService.deleteRoute(uuid);
  }

}
