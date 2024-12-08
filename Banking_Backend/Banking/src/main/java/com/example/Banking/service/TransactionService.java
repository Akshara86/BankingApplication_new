package com.example.Banking.service;

import com.example.Banking.entity.Transaction;
import com.example.Banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Get all transactions for all users (admin access)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();  // Fetch all transactions from the database
    }
}
