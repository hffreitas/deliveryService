package org.example.delivery.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * PointTest class.
 *
 * @author hugo
 */
class PointTest {

  private static final Point point = new Point();

  private static final String NAME = "name";

  private static final String uuid = "c0722484-99a2-4d6c-8af5-4e164b55738e";

  static {
    point.setName(NAME);
    point.setId(UUID.fromString(uuid));
    point.setCreatedAt(Instant.MIN);
    point.setCreatedBy(1L);
    point.setUpdatedAt(Instant.MIN);
    point.setUpdatedBy(1L);
  }

  @Test
  void testEqualsSameObject() {
    assertEquals(point, point);
  }

  @Test
  void testEqualsDifferentClass() {
    assertNotEquals(point, Long.MAX_VALUE);
  }

  @Test
  void testNotNull() {
    assertNotNull(point);
  }

  @Test
  void testDifferentName() {
    Point point1 = new Point();
    point1.setName("name1");
    point1.setId(UUID.fromString(uuid));
    point1.setCreatedAt(Instant.MIN);
    point1.setCreatedBy(1L);
    point1.setUpdatedAt(Instant.MIN);
    point1.setUpdatedBy(1L);
    assertNotEquals(point, point1);
  }

  @Test
  void testDifferentId() {
    Point point1 = new Point();
    point1.setName(NAME);
    point1.setId(UUID.fromString("c0722484-99a2-4d6c-8af5-4e164b557312"));
    point1.setCreatedAt(Instant.MIN);
    point1.setCreatedBy(1L);
    point1.setUpdatedAt(Instant.MIN);
    point1.setUpdatedBy(1L);
    assertNotEquals(point, point1);
  }

  @Test
  void testDifferentCreatedAt() {
    Point point1 = new Point();
    point1.setName(NAME);
    point1.setId(UUID.fromString(uuid));
    point1.setCreatedAt(Instant.MAX);
    point1.setCreatedBy(1L);
    point1.setUpdatedAt(Instant.MIN);
    point1.setUpdatedBy(1L);
    assertNotEquals(point, point1);
  }

  @Test
  void testDifferentCreatedBy() {
    Point point1 = new Point();
    point1.setName(NAME);
    point1.setId(UUID.fromString(uuid));
    point1.setCreatedAt(Instant.MIN);
    point1.setCreatedBy(2L);
    point1.setUpdatedAt(Instant.MIN);
    point1.setUpdatedBy(1L);
    assertNotEquals(point, point1);
  }

  @Test
  void testDifferentUpdateAt() {
    Point point1 = new Point();
    point1.setName(NAME);
    point1.setId(UUID.fromString(uuid));
    point1.setCreatedAt(Instant.MIN);
    point1.setCreatedBy(1L);
    point1.setUpdatedAt(Instant.MAX);
    point1.setUpdatedBy(1L);
    assertNotEquals(point, point1);
  }

  @Test
  void testDifferentUpdateBy() {
    Point point1 = new Point();
    point1.setName(NAME);
    point1.setId(UUID.fromString(uuid));
    point1.setCreatedAt(Instant.MIN);
    point1.setCreatedBy(1L);
    point1.setUpdatedAt(Instant.MIN);
    point1.setUpdatedBy(2L);
    assertNotEquals(point, point1);
  }

  @Test
  void testEquals() {
    Point point1 = new Point();
    point1.setName(NAME);
    point1.setId(UUID.fromString(uuid));
    point1.setCreatedAt(Instant.MIN);
    point1.setCreatedBy(1L);
    point1.setUpdatedAt(Instant.MIN);
    point1.setUpdatedBy(1L);
    assertEquals(point, point1);
  }

  @Test
  void testHashCode() {
    Point point1 = new Point();
    point1.setName(NAME);
    point1.setId(UUID.fromString(uuid));
    point1.setCreatedAt(Instant.MIN);
    point1.setCreatedBy(1L);
    point1.setUpdatedAt(Instant.MIN);
    point1.setUpdatedBy(1L);
    assertEquals(point.hashCode(), point1.hashCode());
  }
}