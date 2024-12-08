package com.example.Banking.repository;

import com.example.Banking.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    // Find transactions by the sender account
    List<Transaction> findByFromAccount(String fromAccount);

    // Find transactions by the recipient account
    List<Transaction> findByToAccount(String toAccount);
}

