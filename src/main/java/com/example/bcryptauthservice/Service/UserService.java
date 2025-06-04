package com.example.bcryptauthservice.Service;

import com.example.bcryptauthservice.DTOs.UserDTO;
import com.example.bcryptauthservice.Exceptions.UserAlreadyExistsException;
import com.example.bcryptauthservice.Exceptions.UserLoginWrongPassword;
import com.example.bcryptauthservice.Exceptions.UserNotRegisteredException;
import com.example.bcryptauthservice.Models.User;
import com.example.bcryptauthservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User registerUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.getUserByEmail(userDTO.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(userDTO.getEmail());
        }

        User newUser = new User();
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(newUser);
    }

    public User loginUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.getUserByEmail(userDTO.getEmail());
        if (!existingUser.isPresent()) {
            throw new UserNotRegisteredException(userDTO.getEmail());
        }

        if(!bCryptPasswordEncoder.matches(userDTO.getPassword(), existingUser.get().getPassword())) {
            throw new UserLoginWrongPassword("Wrong password");
        }

        return existingUser.get();
    }

}
