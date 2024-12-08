package com.example.Banking.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "Accounts")
public class Account {
    @Id
    private String id;  // MongoDB ID (default ID field)

    private String userId;
    private String username;// User ID (given during login)
    private String accountId;  // Account ID (generated, unique identifier)
    private String accountType;  // Savings, Current, etc.
    private Number balance;  // Account balance
    private Status status;  // Status (Created, Updated, Closed)
    private LocalDateTime createdDate;  // Account creation date

    private String aadharNumber;
    private String pan;
    private String gender;

    public enum Status {
        CREATED, UPDATED
    }
}
