package org.example.delivery.security.service;

import org.example.delivery.security.dto.DeliveryUserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * UserService class.
 *
 * @author hugo
 */
@Service
public class UserService {
  /**
   * Transform principal into DeliveryUserDetails.
   *
   * @param token JwtAuthenticationToken
   * @return DeliveryUserDetails
   */
  public DeliveryUserDetails getUser(Object token) {
    DeliveryUserDetails userDetails = new DeliveryUserDetails();
    if (token instanceof JwtAuthenticationToken) {
      JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) token;
      Jwt jwt = (Jwt) jwtAuthenticationToken.getPrincipal();
      userDetails.setAccountId(jwt.getClaim("account_id"));
      userDetails.setClientId(jwt.getClaim("clientId"));
    }
    return userDetails;
  }
}
