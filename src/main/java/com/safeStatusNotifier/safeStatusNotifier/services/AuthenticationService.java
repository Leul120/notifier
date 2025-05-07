package com.safeStatusNotifier.safeStatusNotifier.services;

import com.safeStatusNotifier.safeStatusNotifier.requests.JwtAuthenticationRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.RefreshTokenRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.SignInRequest;
import com.safeStatusNotifier.safeStatusNotifier.requests.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationRequest signup(SignUpRequest signUpRequest);
    JwtAuthenticationRequest signIn(SignInRequest signInRequest);
    JwtAuthenticationRequest refreshToken(RefreshTokenRequest refreshTokenRequest);
}
