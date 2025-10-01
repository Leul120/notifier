package com.safeStatusNotifier.safeStatusNotifier.controllers;



import com.safeStatusNotifier.safeStatusNotifier.entity.User;
import com.safeStatusNotifier.safeStatusNotifier.repositories.UserRepository;
import com.safeStatusNotifier.safeStatusNotifier.requests.UserDto;
import com.safeStatusNotifier.safeStatusNotifier.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//@RestController
//@RequestMapping("/api/users")
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    

        private final UserService userService;

        @QueryMapping
        public User getCurrentUser() {
            return userService.getCurrentUser();
        }

    @QueryMapping
    public User getUserById(@Argument String userId) {
        return userService.getUserById(UUID.fromString(userId));
    }
    }

