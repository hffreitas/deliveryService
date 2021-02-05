package org.example.delivery;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * DeliveryServiceApplication class.
 *
 * @author hugo
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Delivery Service API",
        version = "1.0.0",
        description = "Spring REST API to delivery service"
    )
)
@PropertySource("classpath:application.yml")
public class DeliveryServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(DeliveryServiceApplication.class, args);
  }
}
