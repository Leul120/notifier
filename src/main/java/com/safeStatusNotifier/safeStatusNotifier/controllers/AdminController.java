package com.safeStatusNotifier.safeStatusNotifier.controllers;



import com.safeStatusNotifier.safeStatusNotifier.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api/v1/admin")
@Controller
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController( UserService userService) {

        this.userService = userService;
    }




}

