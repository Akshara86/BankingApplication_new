
package com.example.Banking.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document(collection = "Cards")
public class Card {
    @Id
    private String cardId; // Unique Card ID

    private String userId; // User ID
    private CardType cardType; // Debit or Credit
    private String cardNumber; // Masked Card Number
    private LocalDateTime expiryDate; // Expiry Date
    private String cvvHash; // Encrypted CVV
    private CardStatus status; // ACTIVE/BLOCKED
    private Double cardLimit; // Card spending limit

    public enum CardType {
        DEBIT, CREDIT
    }

    public enum CardStatus {
        ACTIVE, BLOCKED
    }
}
