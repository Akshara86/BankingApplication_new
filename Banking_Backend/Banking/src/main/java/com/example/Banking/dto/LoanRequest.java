package com.example.Banking.dto;

import com.example.Banking.entity.Loan;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoanRequest {
    // Getters and setters
    private String userId;
    private Double loanAmount;
    private Double interestRate;
    private Loan.LoanType loanType; // New field for loan type

    private String documentUrl; // Field for document URL

}
