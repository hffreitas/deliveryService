package org.example.delivery.entities;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Point entity class.
 *
 * @author hugo
 */
@Entity
@Table(name = "points")
@Getter
@Setter
public class Point extends DateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid-ossp")
  private UUID id;
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    Point point = (Point) o;
    return Objects.equals(getId(), point.getId()) && Objects.equals(getName(), point.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getId(), getName());
  }
}
