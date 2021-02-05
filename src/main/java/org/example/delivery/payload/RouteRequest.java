package org.example.delivery.payload;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RouteRequest class.
 *
 * @author hugo
 */
@Getter
@Setter
@NoArgsConstructor
public class RouteRequest {
  private Integer time;
  private Integer cost;
  private UUID origin;
  private UUID destination;
}
