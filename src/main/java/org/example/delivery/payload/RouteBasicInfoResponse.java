package org.example.delivery.payload;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RouteBasicInfoResponse class.
 *
 * @author hugo
 */
@Getter
@Setter
@NoArgsConstructor
public class RouteBasicInfoResponse {
  private UUID id;
  private Long cost;
  private Long time;
  private PointBasicInfoResponse destination;
}
