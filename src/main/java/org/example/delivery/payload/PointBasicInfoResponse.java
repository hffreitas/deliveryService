package org.example.delivery.payload;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * PointBasicInfoResponse class.
 *
 * @author hugo
 */
@Getter
@Setter
@NoArgsConstructor
public class PointBasicInfoResponse {
  private UUID id;
  private String name;
}
