package org.example.delivery.service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.delivery.entities.Point;
import org.example.delivery.exception.NoDataFoundException;
import org.example.delivery.payload.PointRequest;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.PointUpdate;
import org.example.delivery.repository.PointRepository;
import org.example.delivery.util.PointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PointServiceImpl class.
 *
 * @author hugo
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PointServiceImpl implements PointService {

  @Autowired
  private final PointRepository pointRepository;

  @Override
  public Collection<PointResponse> getAllPoints() {
    return pointRepository.findAll().stream().map(PointMapper::mapPointToPointResponse)
        .collect(Collectors.toList());
  }

  @Override
  public PointResponse getPoint(String name) {
    return pointRepository.findByName(name)
        .map(PointMapper::mapPointToPointResponse)
        .orElseThrow(NoDataFoundException::new);
  }

  @Override
  public PointResponse createPoint(PointRequest pointRequest) {
    Point point = new Point();
    point.setName(pointRequest.getName());
    return PointMapper.mapPointToPointResponse(pointRepository.save(point));
  }

  @Override
  public PointResponse updatePoint(UUID id, PointUpdate pointUpdate) {
    Point actual = pointRepository.findById(id).orElseThrow(NoDataFoundException::new);
    actual.setName(pointUpdate.getName());

    return PointMapper.mapPointToPointResponse(pointRepository.save(actual));
  }

  @Override
  public void deletePoint(UUID id) {
    pointRepository.deleteById(id);
  }

}
