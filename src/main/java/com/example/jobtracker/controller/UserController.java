package com.example.jobtracker.controller;
import com.example.jobtracker.dto.ResponseDto;
import com.example.jobtracker.model.User;
import com.example.jobtracker.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<ResponseDto<User>> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            ResponseDto<User> response = new ResponseDto<>(
                    "User retrieved successfully",
                    user.get(),
                    true,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseDto<User> response = new ResponseDto<>(
                    "User not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<ResponseDto<User>> createUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername()) || userService.existsByEmail(user.getEmail())) {
            ResponseDto<User> response = new ResponseDto<>(
                    "Username or email already exists",
                    null,
                    false,
                    HttpStatus.BAD_REQUEST
            );
            return ResponseEntity.badRequest().body(response);
        }
        User savedUser = userService.saveUser(user);
        ResponseDto<User> response = new ResponseDto<>(
                "User created successfully",
                savedUser,
                true,
                HttpStatus.CREATED
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        ResponseDto<List<User>> response = new ResponseDto<>(
                "Users retrieved successfully",
                users,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        ResponseDto<User> response = new ResponseDto<>(
                "User updated successfully",
                updatedUser,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ResponseDto<Void> response = new ResponseDto<>(
                "User deleted successfully",
                null,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
}
