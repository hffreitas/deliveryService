package org.example.delivery.model;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.delivery.entities.Route;

/**
 * RouteMap constructor.
 * It represents a list of points and routes.
 *
 * @author hugo
 */
@Getter
@AllArgsConstructor
public class RouteMap {
  private final Collection<Route> routes;
}
