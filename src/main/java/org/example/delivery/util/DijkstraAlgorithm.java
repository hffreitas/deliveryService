package org.example.delivery.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.example.delivery.entities.Point;
import org.example.delivery.entities.Route;
import org.example.delivery.exception.NoDataFoundException;
import org.example.delivery.model.RouteMap;
import org.example.delivery.model.WeightType;

/**
 * DijkstraAlgorithm class.
 *
 * @author hugo
 */
@Getter
@Setter
public class DijkstraAlgorithm {
  private static final  int ROUTE_MIN_LENGTH = 3;

  private final List<Route> routes;
  private Set<Point> settledNodes;
  private Set<Point> unsettledNodes;
  private Map<Point, Point> predecessors;
  private Map<Point, Integer> distance;
  private final WeightType type;

  public DijkstraAlgorithm(RouteMap routeMap, WeightType type) {
    this.routes = new ArrayList<>(routeMap.getRoutes());
    this.type = type;
  }

  /**
   * Create the path from source to the other points.
   *
   * @param source Point to create the path.
   */
  public void execute(Point source, Point destination) {
    //  Does not allow direct routes
    routes.stream().filter(
        route -> route.getOrigin().equals(source) && route.getDestination().equals(destination))
        .forEach(r -> {
          r.setCost(Integer.MAX_VALUE);
          r.setTime(Integer.MAX_VALUE);
        });

    settledNodes = new HashSet<>();
    unsettledNodes = new HashSet<>();
    distance = new HashMap<>();
    predecessors = new HashMap<>();
    distance.put(source, 0);
    unsettledNodes.add(source);
    while (!unsettledNodes.isEmpty()) {
      Point node = getMininum(unsettledNodes);
      settledNodes.add(node);
      unsettledNodes.remove(node);
      findMinimalDistances(node);
    }
  }

  private void findMinimalDistances(Point node) {
    List<Point> adjacentNodes = getNeighbors(node);
    for (Point target : adjacentNodes) {
      if (getShortestDistance(target)
          > getShortestDistance(node) + getDistance(node, target)) {
        distance.put(target, getShortestDistance(node) + getDistance(node, target));
        predecessors.put(target, node);
        unsettledNodes.add(target);
      }
    }
  }

  private Integer getDistance(Point node, Point target) {
    for (Route route : routes) {
      if (route.getOrigin().equals(node) && route.getDestination().equals(target)) {
        return route.getWeight(type);
      }
    }
    throw new NoDataFoundException();
  }

  private List<Point> getNeighbors(Point node) {
    List<Point> neighbors = new ArrayList<>();
    for (Route route : routes) {
      if (route.getOrigin().equals(node) && !isSettled(route.getDestination())) {
        neighbors.add(route.getDestination());
      }
    }

    return neighbors;
  }

  private boolean isSettled(Point destination) {
    return settledNodes.contains(destination);
  }

  private Point getMininum(Set<Point> vertexes) {
    Point minimun = null;
    for (Point vertex : vertexes) {
      if (minimun == null) {
        minimun = vertex;
      } else {
        if (getShortestDistance(vertex) < getShortestDistance(minimun)) {
          minimun = vertex;
        }
      }
    }

    return minimun;
  }

  private int getShortestDistance(Point destination) {
    Integer d = distance.get(destination);
    if (d == null) {
      return Integer.MAX_VALUE;
    } else {
      return d;
    }
  }

  /**
   * It gets the path from origin to target.
   *
   * @param target target Point
   * @return list of points to go from origin to target
   */
  public Optional<LinkedList<Point>> getPath(Point target) {
    Point step = target;
    if (predecessors.get(step) == null) {
      return Optional.empty();
    }

    LinkedList<Point> path = new LinkedList<>();
    path.add(step);

    while (predecessors.get(step) != null) {
      step = predecessors.get(step);
      path.add(step);
    }

    if (path.size() < ROUTE_MIN_LENGTH) {
      return Optional.empty(); // no direct routes
    }

    Collections.reverse(path);
    return Optional.of(path);
  }
}
