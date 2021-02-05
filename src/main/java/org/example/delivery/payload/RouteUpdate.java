package org.example.delivery.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RouteUpdate class.
 *
 * @author hugo
 */
@Getter
@Setter
@NoArgsConstructor
public class RouteUpdate {
  private int cost;
  private int time;
}
