package org.example.delivery.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.example.delivery.entities.Point;
import org.example.delivery.exception.NoDataFoundException;
import org.example.delivery.payload.PointRequest;
import org.example.delivery.payload.PointResponse;
import org.example.delivery.payload.PointUpdate;
import org.example.delivery.repository.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * PointServiceImpl test class.
 *
 * @author hugo
 */
class PointServiceImplTest {

  @Mock
  private PointRepository pointRepository;
  @InjectMocks
  private PointServiceImpl pointService;

  private static final String name = "name";
  private static final UUID uuid = UUID.fromString("c85fbf21-46f4-4c41-9b7c-670949ccec52");
  private Point point;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    point = new Point();
    point.setName(name);
    point.setCreatedBy(1L);
    point.setUpdatedAt(Instant.now());
    point.setUpdatedBy(1L);
    point.setUpdatedAt(Instant.now());
    point.setId(uuid);
  }

  @Test
  void testGetAllPoints() {
    List<Point> points = new ArrayList<>();
    points.add(point);
    when(pointRepository.findAll()).thenReturn(points);

    Collection<PointResponse> result = pointService.getAllPoints();

    assertEquals(points.size(), result.size());
  }

  @Test
  void testGetPoint() {
    when(pointRepository.findByName(name)).thenReturn(Optional.of(point));

    PointResponse result = pointService.getPoint(name);

    assertEquals(point.getName(), result.getName());
  }

  @Test
  void testGetPointThrowsNoDataFound() {
    when(pointRepository.findByName(name)).thenReturn(Optional.empty());

    assertThrows(NoDataFoundException.class, () -> pointService.getPoint(name));
  }

  @Test
  void testCreatePoint() {
    PointRequest pointRequest = new PointRequest();
    pointRequest.setName(name);
    when(pointRepository.save(any())).thenReturn(point);

    PointResponse result = pointService.createPoint(pointRequest);

    assertEquals(point.getId(), result.getId());
    assertEquals(point.getName(), result.getName());
  }

  @Test
  void testUpdatePoint() {
    PointUpdate pointUpdate = new PointUpdate();
    pointUpdate.setName("name2");
    when(pointRepository.findById(point.getId())).thenReturn(Optional.of(point));
    when(pointRepository.save(any())).thenReturn(point);

    PointResponse result = pointService.updatePoint(point.getId(), pointUpdate);

    assertEquals(result.getId(), point.getId());
    assertEquals(result.getName(), point.getName());
  }

  @Test
  void testUpdatePointThrowsNoDataFoundException() {
    PointUpdate pointUpdate = new PointUpdate();
    pointUpdate.setName("name2");
    when(pointRepository.findById(point.getId())).thenReturn(Optional.empty());

    assertThrows(NoDataFoundException.class,
        () -> pointService.updatePoint(uuid, pointUpdate));
  }

  @Test
  void testDeletePoint() {
    pointService.deletePoint(point.getId());

    verify(pointRepository, times(1)).deleteById(point.getId());
  }
}