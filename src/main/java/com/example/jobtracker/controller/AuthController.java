package com.example.jobtracker.controller;

import com.example.jobtracker.dto.ResponseDto;
import com.example.jobtracker.model.User;
import com.example.jobtracker.model.Role;
import com.example.jobtracker.security.JwtUtils;
import com.example.jobtracker.security.UserPrincipal;
import com.example.jobtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Map<String, Object>>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("token", jwt);
        data.put("username", userDetails.getUsername());
        data.put("email", userDetails.getEmail());
        data.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        data.put("id", userDetails.getId());

        ResponseDto<Map<String, Object>> response = new ResponseDto<>(
                "Login successful",
                data,
                true,
                HttpStatus.OK
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<User>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            ResponseDto<User> response = new ResponseDto<>(
                    "Username is already taken!",
                    null,
                    false,
                    HttpStatus.BAD_REQUEST
            );
            return ResponseEntity.badRequest().body(response);
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            ResponseDto<User> response = new ResponseDto<>(
                    "Email is already in use!",
                    null,
                    false,
                    HttpStatus.BAD_REQUEST
            );
            return ResponseEntity.badRequest().body(response);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                             signUpRequest.getEmail(),
                             signUpRequest.getPassword(),
                             Role.USER);

        User savedUser = userService.saveUser(user);

        ResponseDto<User> response = new ResponseDto<>(
                "User registered successfully!",
                savedUser,
                true,
                HttpStatus.CREATED
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

class LoginRequest {
    private String username;
    private String password;

    public LoginRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class SignupRequest {
    private String username;
    private String email;
    private String password;

    public SignupRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
