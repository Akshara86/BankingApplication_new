package com.example.Banking.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "Loans")
public class Loan {
    @Id
    private String id; // MongoDB ID

    private String userId; // User ID
    private String loanId; // Unique loan identifier
    private Double loanAmount; // Loan amount
    private Double interestRate; // Interest rate on the loan
    private Double totalAmountToPay; // Total amount to pay (loanAmount + interest)
    private LocalDateTime loanStartDate; // Loan start date
    private Status status; // Loan status (e.g., active, closed, defaulted)
    private String documentUrl; // URL to the uploaded document

    private LoanType loanType; // Loan Type (PERSONAL, MORTGAGE, etc.)
    private Boolean approved; // Whether the loan is approved by admin
    private LocalDateTime approvedDate; // The date when the loan was approved

    public enum Status {
        ACTIVE, CLOSED, DEFAULTED
    }

    public enum LoanType {
        PERSONAL, MORTGAGE, AUTO, STUDENT
    }
}
