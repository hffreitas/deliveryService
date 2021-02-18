package org.example.delivery.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.example.delivery.security.dto.DeliveryUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

class UserServiceTest {

  private UserService userService = new UserService();

  @Test
  void getUser() {
    Map<String, Object> claims = new HashMap<>();
    claims.put("clientId", "clientId");
    claims.put("account_id", "accountId");
    Map<String, Object> headers = new HashMap<>();
    headers.put("header", "header");
    Jwt jwt = new Jwt("token", Instant.MIN, Instant.MAX, headers, claims);
    JwtAuthenticationToken token = new JwtAuthenticationToken(jwt);

    DeliveryUserDetails deliveryUserDetails = userService.getUser(token);

    assertEquals("clientId", deliveryUserDetails.getClientId());
    assertEquals("accountId", deliveryUserDetails.getAccountId());
  }
}