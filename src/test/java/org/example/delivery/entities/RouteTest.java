package org.example.delivery.entities;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;
import org.example.delivery.model.WeightType;
import org.junit.jupiter.api.Test;

/**
 * RouteTest class.
 *
 * @author hugo
 */
class RouteTest {

  private static final String uuid = "c0722484-99a2-4d6c-8af5-4e164b55738e";

  private static final Route route = new Route();

  static {
    route.setId(UUID.fromString(uuid));
    route.setOrigin(new Point());
    route.setDestination(new Point());
    route.setCost(Integer.MAX_VALUE);
    route.setTime(Integer.MAX_VALUE);
  }

  @Test
  void testEqualsSameObject() {
    assertEquals(route, route);
  }

  @Test
  void testEqualsDifferentTypes() {
    assertNotEquals(route, Long.MAX_VALUE);
  }

  @Test
  void testNotNull() {
    assertNotNull(route);
  }

  @Test
  void testEqualsSuperIsDifferent() {
    Route route1 = new Route();
    route1.setId(UUID.fromString(uuid));
    route1.setOrigin(new Point());
    route1.setDestination(new Point());
    route1.setCost(Integer.MAX_VALUE);
    route1.setTime(Integer.MAX_VALUE);
    route1.setUpdatedBy(Long.MAX_VALUE);

    assertNotEquals(route, route1);
  }

  @Test
  void testEqualsDifferentId() {
    Route route1 = new Route();
    route1.setId(UUID.randomUUID());
    route1.setOrigin(new Point());
    route1.setDestination(new Point());
    route1.setCost(Integer.MAX_VALUE);
    route1.setTime(Integer.MAX_VALUE);

    assertNotEquals(route, route1);
  }

  @Test
  void testEqualsDifferentOrigin() {
    Point point = new Point();
    point.setName("name");
    Route route1 = new Route();
    route1.setId(UUID.fromString(uuid));
    route1.setOrigin(point);
    route1.setDestination(new Point());
    route1.setCost(Integer.MAX_VALUE);
    route1.setTime(Integer.MAX_VALUE);

    assertNotEquals(route, route1);
  }

  @Test
  void testEqualsDifferentDestination() {
    Point point = new Point();
    point.setName("name");
    Route route1 = new Route();
    route1.setId(UUID.fromString(uuid));
    route1.setOrigin(new Point());
    route1.setDestination(point);
    route1.setCost(Integer.MAX_VALUE);
    route1.setTime(Integer.MAX_VALUE);

    assertNotEquals(route, route1);
  }

  @Test
  void testEqualsDifferentCost() {
    Route route1 = new Route();
    route1.setId(UUID.fromString(uuid));
    route1.setOrigin(new Point());
    route1.setDestination(new Point());
    route1.setCost(Integer.MIN_VALUE);
    route1.setTime(Integer.MAX_VALUE);

    assertNotEquals(route, route1);
  }

  @Test
  void testEqualsDifferentTime() {
    Route route1 = new Route();
    route1.setId(UUID.fromString(uuid));
    route1.setOrigin(new Point());
    route1.setDestination(new Point());
    route1.setCost(Integer.MAX_VALUE);
    route1.setTime(Integer.MIN_VALUE);

    assertNotEquals(route, route1);
  }

  @Test
  void testEquals() {
    Route route1 = new Route();
    route1.setId(UUID.fromString(uuid));
    route1.setOrigin(new Point());
    route1.setDestination(new Point());
    route1.setCost(Integer.MAX_VALUE);
    route1.setTime(Integer.MAX_VALUE);

    assertEquals(route, route1);
  }

  @Test
  void testHashCode() {
    Route route1 = new Route();
    route1.setId(UUID.fromString(uuid));
    route1.setOrigin(new Point());
    route1.setDestination(new Point());
    route1.setCost(Integer.MAX_VALUE);
    route1.setTime(Integer.MAX_VALUE);

    assertEquals(route.hashCode(), route1.hashCode());
  }

  @Test
  void testGetWeightCost() {
    assertEquals(route.getWeight(WeightType.COST), route.getCost());
  }

  @Test
  void testGetWeightTime() {
    assertEquals(route.getWeight(WeightType.TIME), route.getTime());
  }

}