package com.example.bcryptauthservice.Controller;

import com.example.bcryptauthservice.DTOs.UserDTO;
import com.example.bcryptauthservice.Models.User;
import com.example.bcryptauthservice.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(UserDTO userDTO){
        return userService.registerUser(userDTO);
    }

    @PostMapping("/login")
    public User login(UserDTO userDTO){
        return userService.loginUser(userDTO);
    }
}
