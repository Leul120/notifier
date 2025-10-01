package com.safeStatusNotifier.safeStatusNotifier.services;

import com.safeStatusNotifier.safeStatusNotifier.entity.User;

import com.safeStatusNotifier.safeStatusNotifier.requests.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDetailsService userDetailsService();
    void deleteUser(UUID id);
    User getCurrentUser();
    User getUserById(UUID userId);


}
