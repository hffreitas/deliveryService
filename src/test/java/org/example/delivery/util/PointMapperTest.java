package org.example.delivery.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;
import java.util.UUID;
import org.example.delivery.entities.Point;
import org.example.delivery.payload.PointBasicInfoResponse;
import org.example.delivery.payload.PointResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PointMapperTest {

  private Point point = new Point();

  @BeforeEach
  void setup() {
    point.setId(UUID.randomUUID());
    point.setName("name");
    point.setUpdatedAt(Instant.MAX);
    point.setUpdatedBy(Long.MAX_VALUE);
    point.setCreatedBy(Long.MAX_VALUE);
    point.setCreatedAt(Instant.MAX);
  }

  @Test
  void mapPointToPointResponse_NullPoint_NullPointResponse() {
    assertNull(PointMapper.mapPointToPointResponse(null));
  }

  @Test
  void mapPointToPointResponse_Point_PointResponse() {

    PointResponse pointResponse = PointMapper.mapPointToPointResponse(point);
    assertEquals(point.getName(), pointResponse.getName());
    assertEquals(point.getId(), pointResponse.getId());
  }

  @Test
  void mapPointToPointBasicInfoResponse_NullPoint_NullPointBasicInfoResponse() {
    assertNull(PointMapper.mapPointToPointBasicInfoResponse(null));
  }

  @Test
  void mapPointToPointBasicInfoResponse_Point_PointBasicInfoResponse() {
    PointBasicInfoResponse response = PointMapper.mapPointToPointBasicInfoResponse(point);

    assertEquals(point.getId(), response.getId());
    assertEquals(point.getName(), response.getName());
  }
}