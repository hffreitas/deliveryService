package org.example.delivery.util;

import org.example.delivery.entities.Point;
import org.example.delivery.payload.PointBasicInfoResponse;
import org.example.delivery.payload.PointResponse;

/**
 * PointMapper class.
 *
 * @author hugo
 */
public class PointMapper {

  private PointMapper() {
    throw new IllegalStateException("PointMapper class");
  }

  /**
   * maps Point to PointResponse.
   *
   * @param point Point object
   * @return PointResponse object
   */
  public static PointResponse mapPointToPointResponse(Point point) {
    if (point == null) {
      return null;
    }
    PointResponse pointResponse = new PointResponse();
    pointResponse.setId(point.getId());
    pointResponse.setName(point.getName());
    return pointResponse;
  }

  /**
   * maps point to pointBasicInfoResponse.
   *
   * @param point Point object
   * @return PointBasicInfoResponse
   */
  public static PointBasicInfoResponse mapPointToPointBasicInfoResponse(Point point) {
    if (point == null) {
      return null;
    }
    PointBasicInfoResponse pointBasicInfoResponse = new PointBasicInfoResponse();
    pointBasicInfoResponse.setId(point.getId());
    pointBasicInfoResponse.setName(point.getName());

    return pointBasicInfoResponse;
  }
}
