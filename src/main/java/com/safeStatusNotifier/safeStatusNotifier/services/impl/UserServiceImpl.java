package com.safeStatusNotifier.safeStatusNotifier.services.impl;



import com.safeStatusNotifier.safeStatusNotifier.entity.Role;
import com.safeStatusNotifier.safeStatusNotifier.entity.User;

import com.safeStatusNotifier.safeStatusNotifier.repositories.UserRepository;
import com.safeStatusNotifier.safeStatusNotifier.requests.*;
import com.safeStatusNotifier.safeStatusNotifier.services.JwtService;
import com.safeStatusNotifier.safeStatusNotifier.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;

    }



    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found!"));
            }
        };
    }
    @Override
    public void deleteUser(UUID id){
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToUserDto(user);
    }
    @Override
    public UserDto getUserById(String  userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToUserDto(user);
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




