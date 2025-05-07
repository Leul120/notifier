package com.safeStatusNotifier.safeStatusNotifier.services.impl;

import com.safeStatusNotifier.safeStatusNotifier.entity.Role;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.repositories.UserRepository;
import com.safeStatusNotifier.safeStatusNotifier.requests.*;
import com.safeStatusNotifier.safeStatusNotifier.services.AuthenticationService;
import com.safeStatusNotifier.safeStatusNotifier.services.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public AuthenticationServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager,JwtService jwtService){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
    }




    @Override
    public JwtAuthenticationRequest signup(SignUpRequest signUpRequest) {
        Optional<User> oldUser=userRepository.findByEmail(signUpRequest.getEmail());
        System.out.println(oldUser);
        if(oldUser.isEmpty()) {
            User user = new User();
            user.setName(signUpRequest.getName());
            user.setEmail(signUpRequest.getEmail());
            user.setPhone(signUpRequest.getPhone());
            user.setRole(Role.USER);
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            User savedUser= userRepository.save(user);
            String jwt;
            String refreshToken;
            try{
                jwt=jwtService.generateToken(savedUser);
                refreshToken=jwtService.generateRefreshToken(new HashMap<>(),savedUser);
            }catch (Exception e){
                throw new RuntimeException("Token generation failed");
            }

            JwtAuthenticationRequest jwtAuthenticationRequest=new JwtAuthenticationRequest();
            jwtAuthenticationRequest.setAccessToken(jwt);
            jwtAuthenticationRequest.setUser(mapToUserDto(user));
            return jwtAuthenticationRequest;
        }else{
            throw new IllegalArgumentException("user already exists!");
        }
    }

    @Override
    public JwtAuthenticationRequest signIn(SignInRequest signInRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword())
            );

        }catch (AuthenticationException ex){
            throw new IllegalArgumentException("Invalid email or password!");
        }

        User user=userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(()->new IllegalArgumentException("User not Found!"));
        String jwt;
        String refreshToken;
        try{
            jwt=jwtService.generateToken(user);
            refreshToken=jwtService.generateRefreshToken(new HashMap<>(),user);
        }catch (Exception e){
            throw new RuntimeException("Token generation failed");
        }

        JwtAuthenticationRequest jwtAuthenticationRequest=new JwtAuthenticationRequest();
        jwtAuthenticationRequest.setAccessToken(jwt);
        jwtAuthenticationRequest.setUser(mapToUserDto(user));
        return jwtAuthenticationRequest;
    }

    @Override
    public JwtAuthenticationRequest refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail=jwtService.extractUserName(refreshTokenRequest.getToken());
        User user=userRepository.findByEmail(userEmail).orElseThrow(()->new IllegalArgumentException("User not Found!"));
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(),user)){
            Map<String,Object> extraClaims=new HashMap<>();
            var jwt=jwtService.generateRefreshToken(extraClaims,user);
            JwtAuthenticationRequest jwtAuthenticationRequest=new JwtAuthenticationRequest();
            jwtAuthenticationRequest.setAccessToken(jwt);
            jwtAuthenticationRequest.setUser(mapToUserDto(user));
            return jwtAuthenticationRequest;
        }
        return null;
    }

    private UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        return dto;
    }
}
