package com.example.Banking.service;

import com.example.Banking.entity.Loan;
import com.example.Banking.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    // Apply for a loan with document URL
    public Loan applyLoan(String userId, Double loanAmount, Double interestRate, Loan.LoanType loanType, String documentUrl) {
        // Calculate the total amount to pay (loanAmount + interest rate)
        Double totalAmountToPay = loanAmount + (loanAmount * interestRate / 100);

        Loan loan = new Loan();
        loan.setUserId(userId);
        loan.setLoanId(userId + "_" + System.currentTimeMillis()); // Unique loan ID
        loan.setLoanAmount(loanAmount);
        loan.setInterestRate(interestRate);
        loan.setTotalAmountToPay(totalAmountToPay);
        loan.setLoanStartDate(LocalDateTime.now());
        loan.setStatus(Loan.Status.ACTIVE);
        loan.setLoanType(loanType);
        loan.setApproved(false); // Initially not approved
        loan.setDocumentUrl(documentUrl); // Store the provided document URL

        return loanRepository.save(loan);
    }

    // Admin approves the loan
    public Loan approveLoan(String loanId) {
        Optional<Loan> loanOptional = loanRepository.findById(loanId);
        if (loanOptional.isPresent()) {
            Loan loan = loanOptional.get();
            if (loan.getApproved()) {
                throw new RuntimeException("Loan is already approved.");
            }

            loan.setApproved(true);
            loan.setApprovedDate(LocalDateTime.now());
            return loanRepository.save(loan);
        } else {
            throw new RuntimeException("Loan not found.");
        }
    }

    // Get all loans by userId
    public List<Loan> getLoansByUserId(String userId) {
        return loanRepository.findByUserId(userId);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll(); // Fetch all loans from the repository
    }
}
