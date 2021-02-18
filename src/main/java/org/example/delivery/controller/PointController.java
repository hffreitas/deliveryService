package org.example.delivery.controller;

import static org.example.delivery.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.delivery.payload.PointRequest;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.PointUpdate;
import org.example.delivery.security.dto.CurrentUser;
import org.example.delivery.security.dto.DeliveryUserDetails;
import org.example.delivery.service.PointService;
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
 * PointController class.
 *
 * @author hugo
 */
@RestController
@RequestMapping("/v1/points")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PointController {

  @Autowired
  private final PointService pointService;

  @Operation(summary = "Get list of points")
  @GetMapping("/all")

  public Collection<PointResponse> getAllPoints(@CurrentUser DeliveryUserDetails deliveryUserDetails) {
    return pointService.getAllPoints();
  }

  @Operation(summary = "Get book by name")
  @GetMapping("/{name}")
  public PointResponse getPointByName(@PathVariable("name") String name) {
    return pointService.getPoint(name);
  }

  @Operation(summary = "Create a point", security = {
      @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @PostMapping
  @PreAuthorize("hasRole('Admin')")
  public PointResponse createPoint(@RequestBody PointRequest pointRequest) {
    return pointService.createPoint(pointRequest);
  }

  @Operation(summary = "Update a point", security = {
      @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('Admin')")
  public PointResponse updatePoint(
      @PathVariable("id") UUID id,
      @RequestBody PointUpdate pointUpdate) {
    return pointService.updatePoint(id, pointUpdate);
  }

  @Operation(summary = "Delete a point", security = {
      @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('Admin')")
  public void deletePoint(@PathVariable("id") UUID id) {
    pointService.deletePoint(id);
  }

}
