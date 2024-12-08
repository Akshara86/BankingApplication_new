package com.example.Banking.service;

import com.example.Banking.dto.ResponseMessage;
import com.example.Banking.entity.User;
import com.example.Banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    private final BCryptPasswordEncoder encoder =new BCryptPasswordEncoder(12);

    public String createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "Email already exists!";
        }

        if(user.getUsername() == null ){
            return "User Name is Required";
        }
        if(user.getEmail() == null ){
            return "Email is Required";
        }
        if(user.getPassword() == null ){
            return "Password is Required";
        }

        if(user.getRole() == null ){
            return "Role is Required";
        }

        user.setPassword(encoder.encode(user.getPassword())); //get the password and encode

        userRepository.save(user);
        return "User created successfully!";
    }

    // Login user and validate credentials
    // Login User
    public ResponseMessage loginUser(User user) {
        if (user.getRole() == null) {
            return new ResponseMessage("error", "Role is Required");
        }

        // Convert role to lowercase for validation
        String role = user.getRole().toLowerCase();

        if (user.getEmail() == null) {
            return new ResponseMessage("error", "Email is Required");
        }
        if (user.getPassword() == null) {
            return new ResponseMessage("error", "Password is Required");
        }

        // Validate role
        if (!role.equals("customer") && !role.equals("admin")) {
            return new ResponseMessage("error", "Invalid role provided");
        }

        // Find the user by email
        User existingUser = userRepository.findByEmail(user.getEmail());

        // If the user is found
        if (existingUser != null) {
            // Check if the roles match
            if (!existingUser.getRole().toLowerCase().equals(role)) {
                return new ResponseMessage("error", "Role mismatch");
            }

            // Use BCrypt to compare the entered password with the stored hashed password
            if (encoder.matches(user.getPassword(), existingUser.getPassword())) {
                // Successfully logged in, set isActive to true
                existingUser.setIsActive(true);
                userRepository.save(existingUser);  // Update the user status

                // Return success response with id and username
                ResponseMessage response = new ResponseMessage("success", "Login successful");
                Map<String, Object> data = new HashMap<>();
                data.put("id", existingUser.getId());
                data.put("username", existingUser.getUsername());

                // Add additional data (id and username) to the response
                response.setData(data);
                return response;
            } else {
                return new ResponseMessage("error", "Invalid credentials");
            }
        } else {
            return new ResponseMessage("error", "Email not found");
        }
    }

    public ResponseMessage getUserDetails(String userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return new ResponseMessage("error", "User not found");
        }

        // Return success response with user data
        ResponseMessage response = new ResponseMessage("success", "User details fetched successfully");
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());
        data.put("isActive", user.getIsActive());

        response.setData(data);
        return response;
    }

    // Logout user and set isActive to false
    public ResponseMessage logoutUser(String userId) {
        // Find the user by ID (assuming userId is a String)
        Optional<User> userOptional = userRepository.findById(userId);  // Assuming the ID is a String

        // If the user is found
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            // Check if the user is already logged out (isActive is false)
            if (!existingUser.getIsActive()) {
                return new ResponseMessage("error", "User is not logged in");
            }

            // Set isActive to false on logout
            existingUser.setIsActive(false);
            userRepository.save(existingUser);  // Update the user status

            return new ResponseMessage("success", "Logout successful");
        } else {
            // If the user with the provided ID is not found
            return new ResponseMessage("error", "User not found");
        }
    }

    public long getTotalUserCount() {
        return userRepository.count();  // Assumes you have a JpaRepository
    }

    public long getActiveUserCount() {
        return userRepository.countByIsActive(true);  // Custom method
    }
}