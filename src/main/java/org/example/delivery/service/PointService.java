package org.example.delivery.service;

import java.util.Collection;
import java.util.UUID;
import org.example.delivery.payload.PointRequest;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.PointUpdate;

/**
 * PointService class.
 *
 * @author hugo
 */
public interface PointService {
  Collection<PointResponse> getAllPoints();

  PointResponse getPoint(String name);

  PointResponse createPoint(PointRequest pointRequest);

  PointResponse updatePoint(UUID id, PointUpdate point);

  void deletePoint(UUID id);
}
