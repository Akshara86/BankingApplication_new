package com.example.Banking.repository;

import com.example.Banking.entity.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    List<Account> findByUserId(String userId);  // Find all accounts by userId

    Optional<Account> findByAccountId(String accountId);  // Find account by accountId

    // Custom query to find accounts by userId and accountType
    List<Account> findByUserIdAndAccountType(String userId, String accountType);
    void deleteByAccountId(String accountId);
}
