package com.example.Banking.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data

@Document(collection = "Transactions")

public class Transaction {

    @Id
    private String id;  // MongoDB ID

    private String fromAccount;  // From Account
    private String toAccount;  // To Account (nullable for deposit and withdrawal)
    private Double amount;  // Transaction amount
    private String type;  // Type: "withdraw", "deposit", "transfer"
    private String status;  // Status: "success", "failed"
    private LocalDateTime timestamp;  // Transaction timestamp
}