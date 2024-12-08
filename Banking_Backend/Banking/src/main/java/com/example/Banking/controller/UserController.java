package com.example.Banking.controller;

import com.example.Banking.dto.ResponseMessage;
import com.example.Banking.entity.User;
import com.example.Banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody User user) {
        String result = userService.createUser(user);
        if (result.equals("User created successfully!")) {
            // Return success response with appropriate message
            return ResponseEntity.ok(new ResponseMessage("success", result));
        } else {
            // Return error response with the failure message
            return ResponseEntity.badRequest().body(new ResponseMessage("error", result));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> loginUser(@RequestBody User user) {
        ResponseMessage result = userService.loginUser(user);

        // Check if login was successful
        if ("success".equals(result.getStatus())) {
            return ResponseEntity.ok(result);  // Return a 200 OK response with the result
        } else {
            return ResponseEntity.badRequest().body(result);  // Return a 400 Bad Request response with the error message
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<ResponseMessage> logoutUser(@RequestParam String userId) {
        ResponseMessage result = userService.logoutUser(userId);

        if (result.getStatus().equals("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @GetMapping("/{userId}")
    public ResponseMessage getUserDetails(@PathVariable String userId) {
        return userService.getUserDetails(userId);
    }

    @GetMapping("/admin/user-count")
    public ResponseEntity<ResponseMessage> getUserCounts() {
        long totalUsers = userService.getTotalUserCount();
        long activeUsers = userService.getActiveUserCount();

        // Use a Map to store the data
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalUsers", totalUsers);
        data.put("activeUsers", activeUsers);

        // Wrap the data in a ResponseMessage
        ResponseMessage response = new ResponseMessage("success", "User counts retrieved successfully", data);

        return ResponseEntity.ok(response);
    }
}
