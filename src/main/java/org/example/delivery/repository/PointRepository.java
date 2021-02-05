package org.example.delivery.repository;

import java.util.Optional;
import java.util.UUID;
import org.example.delivery.entities.Point;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PointRepository interface.
 *
 * @author hugo
 */
public interface PointRepository extends JpaRepository<Point, UUID> {
  Optional<Point> findByName(String name);
}
