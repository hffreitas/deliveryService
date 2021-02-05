package org.example.delivery.entities;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.delivery.model.WeightType;

/**
 * Route Entity class.
 *
 * @author hugo
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "routes")
@Entity
public class Route extends DateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid-ossp")
  private UUID id;
  @ManyToOne(targetEntity = Point.class)
  @JoinColumn(name = "origin")
  private Point origin;
  @ManyToOne(targetEntity = Point.class)
  @JoinColumn(name = "destination")
  private Point destination;
  private Integer cost;
  private Integer time;

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

    Route route = (Route) o;
    return Objects.equals(getId(), route.getId())
        && Objects.equals(getOrigin(), route.getOrigin())
        && Objects.equals(getDestination(), route.getDestination())
        && Objects.equals(getCost(), route.getCost())
        && Objects.equals(getTime(), route.getTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getId(), getOrigin(), getDestination(), getCost(), getTime());
  }

  /**
   * It returns the weight according the type.
   *
   * @param type Type of weight
   * @return weight
   */
  public Integer getWeight(WeightType type) {
    if (type.equals(WeightType.COST)) {
      return cost;
    } else {
      return time;
    }
  }
}
