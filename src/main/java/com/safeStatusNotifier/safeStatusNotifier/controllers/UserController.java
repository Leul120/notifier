package com.safeStatusNotifier.safeStatusNotifier.controllers;



import com.safeStatusNotifier.safeStatusNotifier.repositories.UserRepository;
import com.safeStatusNotifier.safeStatusNotifier.requests.UserDto;
import com.safeStatusNotifier.safeStatusNotifier.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    

        private final UserService userService;

        // Grant access from a user to a parent
        @GetMapping("/me")
        public ResponseEntity<UserDto> getCurrentUser() {
            return ResponseEntity.ok(userService.getCurrentUser());
        }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }
    }

