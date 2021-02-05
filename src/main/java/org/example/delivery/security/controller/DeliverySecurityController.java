package org.example.delivery.security.controller;

import org.example.delivery.security.dto.DeliveryUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * DeliverySecurityController class.
 *
 * @author hugo
 */
@ControllerAdvice
public class DeliverySecurityController {
  /**
   * It converts the principal into DeliveryUserDetails.
   *
   * @param authentication Authentication entity
   * @return userdetails
   */
  @ModelAttribute
  public DeliveryUserDetails userDetails(Authentication authentication) {
    Jwt principal = (Jwt) authentication.getPrincipal();
    DeliveryUserDetails userDetails = new DeliveryUserDetails();
    userDetails.setAccountId(principal.getClaimAsString("account_id"));
    userDetails.setClientId(principal.getClaimAsString("clientId"));

    return userDetails;
  }
}
