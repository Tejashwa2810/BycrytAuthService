package com.example.bcryptauthservice.Service;

import com.example.bcryptauthservice.DTOs.UserDTO;
import com.example.bcryptauthservice.Exceptions.UserAlreadyExistsException;
import com.example.bcryptauthservice.Exceptions.UserLoginWrongPassword;
import com.example.bcryptauthservice.Exceptions.UserNotRegisteredException;
import com.example.bcryptauthservice.Models.User;
import com.example.bcryptauthservice.Models.UserSession;
import com.example.bcryptauthservice.Repository.UserRepository;
import com.example.bcryptauthservice.Repository.UserSessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSessionRepository sessionRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SecretKey secretKey;

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

    public Pair<User, String> loginUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.getUserByEmail(userDTO.getEmail());
        if (!existingUser.isPresent()) {
            throw new UserNotRegisteredException(userDTO.getEmail());
        }

        if(!bCryptPasswordEncoder.matches(userDTO.getPassword(), existingUser.get().getPassword())) {
            throw new UserLoginWrongPassword("Wrong password");
        }

        Map<String, Object> claim = new HashMap<>(); // Another name for "Payload" is "claim"
        claim.put("email", userDTO.getEmail());
        Long nowInMillis = System.currentTimeMillis();
        claim.put("iat", nowInMillis); // Issued at
        claim.put("exp", nowInMillis + 100000);
        claim.put("iss", "square_uas"); // Issued Source

//        MacAlgorithm algorithm = Jwts.SIG.HS256;
//        SecretKey secretKey = algorithm.key().build();

        String token = Jwts.builder().claims(claim).signWith(secretKey).compact();


        /*

        ".claims(claim)" is adding the payload.
        ".signWith(secretKey)" is adding the signature to the token.
        ".compact()" is closely packing everything together.

         */

        UserSession userSession = new UserSession();
        userSession.setToken(token);
        userSession.setUser(existingUser.get());
        sessionRepository.save(userSession);

        return Pair.of(existingUser.get(), token);
    }

    public boolean validateToken(String token, int userId) {
        Optional<UserSession> optionalUserSession = sessionRepository.findByTokenAndId(token, userId);

        if (optionalUserSession.isEmpty()) {
            return false;
        }

        UserSession userSession = optionalUserSession.get();
        String persistedToken = userSession.getToken();

        //Parsing token to get payload to get expiry
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseClaimsJws(persistedToken).getBody();

        Long expiry = (Long) claims.get("exp");
        Long currentTime = System.currentTimeMillis();

        if(currentTime > expiry) {
            return false;
        }

        return true;
    }

}
