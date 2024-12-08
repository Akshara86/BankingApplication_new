package com.example.Banking.repository;

import com.example.Banking.entity.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends MongoRepository<Loan, String> {
    // Custom query to find loans by userId
    List<Loan> findByUserId(String userId);

}
