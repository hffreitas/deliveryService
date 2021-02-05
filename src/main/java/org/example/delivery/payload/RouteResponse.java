package org.example.delivery.payload;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RouteResponse class.
 *
 * @author hugo
 */
@Getter
@Setter
@NoArgsConstructor
public class RouteResponse {
  private UUID id;
  private Integer time;
  private Integer cost;
  private PointBasicInfoResponse origin;
  private PointBasicInfoResponse destination;
}
