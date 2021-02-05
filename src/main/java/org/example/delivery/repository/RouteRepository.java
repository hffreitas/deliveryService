package org.example.delivery.repository;

import java.util.UUID;
import org.example.delivery.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RouteRepository interface.
 *
 * @author hugo
 */
public interface RouteRepository extends JpaRepository<Route, UUID> {
}
