package com.poluhin.ss.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenCache {

  private String username;
  private AuthResponse authResponse;

}
