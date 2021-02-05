package org.example.delivery.controller;

import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.delivery.payload.PointRequest;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.PointUpdate;
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

  @GetMapping("/all")
  @PreAuthorize("hasRole('Admin')")
  public Collection<PointResponse> getAllPoints() {
    return pointService.getAllPoints();
  }

  @GetMapping("/{name}")
  public PointResponse getPointByName(@PathVariable("name") String name) {
    return pointService.getPoint(name);
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public PointResponse createPoint(@RequestBody PointRequest pointRequest) {
    return pointService.createPoint(pointRequest);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public PointResponse updatePoint(
      @PathVariable("id") UUID id,
      @RequestBody PointUpdate pointUpdate) {
    return pointService.updatePoint(id, pointUpdate);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public void deletePoint(@PathVariable("id") UUID id) {
    pointService.deletePoint(id);
  }

}
