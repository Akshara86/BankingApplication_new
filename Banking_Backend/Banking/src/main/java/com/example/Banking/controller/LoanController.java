package com.example.Banking.controller;

import com.example.Banking.dto.ResponseMessage;
import com.example.Banking.dto.LoanRequest;
import com.example.Banking.entity.Loan;
import com.example.Banking.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // Apply for a loan with document URL
    @PostMapping("/apply")
    public ResponseEntity<?> applyLoan(@RequestBody LoanRequest request) {
        try {
            Loan loan = loanService.applyLoan(
                    request.getUserId(),
                    request.getLoanAmount(),
                    request.getInterestRate(),
                    request.getLoanType(),
                    request.getDocumentUrl()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(loan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Failed to apply for loan: " + e.getMessage()));
        }
    }

    // Admin approves the loan
    @PutMapping("/approve/{loanId}")
    public ResponseEntity<ResponseMessage> approveLoan(@PathVariable String loanId) {
        try {
            Loan loan = loanService.approveLoan(loanId);
            return ResponseEntity.ok(new ResponseMessage("success", "Loan approved."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Failed to approve loan: " + e.getMessage()));
        }
    }

    // Get all loans by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getLoansByUserId(@PathVariable String userId) {
        try {
            List<Loan> loans = loanService.getLoansByUserId(userId);
            if (loans.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("error", "No loans found for userId: " + userId));
            }
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Error fetching loans for userId: " + userId));
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllLoans() {
        try {
            List<Loan> loans = loanService.getAllLoans();
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Error fetching loans: " + e.getMessage()));
        }
    }
}
