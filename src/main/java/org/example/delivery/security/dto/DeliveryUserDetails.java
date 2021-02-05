package org.example.delivery.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DeliveryUserDetails class.
 *
 * @author hugo
 */
@Getter
@Setter
@NoArgsConstructor
public class DeliveryUserDetails {
  private String clientId;
  private String accountId;
}
