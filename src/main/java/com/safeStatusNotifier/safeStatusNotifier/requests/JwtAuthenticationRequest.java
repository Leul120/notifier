package com.safeStatusNotifier.safeStatusNotifier.requests;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtAuthenticationRequest implements Serializable {
    private UserDto user;
    private String accessToken;
}
