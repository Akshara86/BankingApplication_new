package com.example.Banking.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id; // MongoDB will automatically generate the _id field

    private String username;


    private String email;
    private String password;

    private Boolean isActive = false; // Default value set to false

    private String role;
}
