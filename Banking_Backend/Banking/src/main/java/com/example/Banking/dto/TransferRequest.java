package com.example.Banking.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class TransferRequest {
    private String fromAccountId; // From Account ID for transfer
    private String toAccountId;   // To Account ID for transfer
    private Double amount;        // Amount for transfer
}
