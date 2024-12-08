package com.example.Banking.service;

import com.example.Banking.entity.Account;
import com.example.Banking.entity.Transaction;
import com.example.Banking.entity.User;
import com.example.Banking.repository.AccountRepository;
import com.example.Banking.repository.TransactionRepository;
import com.example.Banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // Open a new account
    public Account openAccount(String userId, String accountType, Number initialBalance,
                               String aadharNumber, String pan, String gender) {
        // Check if an account with the same userId and accountType already exists
        List<Account> existingAccounts = accountRepository.findByUserIdAndAccountType(userId, accountType);
        if (!existingAccounts.isEmpty()) {
            throw new IllegalArgumentException("An account with this userId and accountType already exists.");
        }

        // Fetch the user's information to get the userName
        Optional<User> userOptional = userRepository.findById(userId); // Fetch the user by userId
        String userName; // Get userName, default if not found
        userName = userOptional.map(User::getUsername).orElse("Unknown User");

        // Create a new account
        Account account = new Account();
        account.setUserId(userId);
        account.setUsername(userName); // Set the userName in the account
        account.setAccountType(accountType);
        account.setBalance(initialBalance);
        account.setStatus(Account.Status.CREATED);
        account.setAadharNumber(aadharNumber);
        account.setPan(pan);
        account.setGender(gender);
        account.setCreatedDate(LocalDateTime.now());
        account.setAccountId(userId + "_" + LocalDateTime.now().toString());  // Unique accountId

        // Save the account to the repository
        return accountRepository.save(account);
    }

    // Update an account's balance
    public Account updateAccount(String accountId, Number newBalance) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setBalance(newBalance);
            account.setStatus(Account.Status.UPDATED);
            return accountRepository.save(account);
        }
        return null;
    }

    // Close an account (only if balance is 0)
    public boolean closeAccount(String accountId) {
        Optional<Account> accountOptional = accountRepository.findByAccountId(accountId);  // Use accountId to find the account

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            // Check if balance is zero
            if (account.getBalance().doubleValue() == 0) {
                // Delete the account by accountId (not _id)
                accountRepository.deleteByAccountId(accountId);  // Delete by accountId

                return true;  // Account closed and deleted successfully
            } else {
                throw new RuntimeException("Cannot close account with non-zero balance. Please withdraw funds first.");
            }
        }

        // Return false if the account does not exist
        return false;
    }

    // Get account by accountId
    public Account getAccount(String accountId) {
        return accountRepository.findByAccountId(accountId).orElse(null);
    }

    // Get account by userId
    public List<Account> getAccountByUserId(String userId) {
        return accountRepository.findByUserId(userId);  // Assuming accountRepository returns a list of accounts
    }

    // Withdraw funds from an account
    public void withdraw(String accountId, Double amount) {
        Optional<Account> accountOptional = accountRepository.findByAccountId(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            if (account.getBalance().doubleValue() >= amount) {
                // Update the balance
                account.setBalance(account.getBalance().doubleValue() - amount);

                // Save the updated account
                accountRepository.save(account);

                // Record the transaction (ensure it's being saved in the Transaction repository)
                createTransaction(accountId, null, amount, "withdraw", "success");

            } else {
                // Insufficient balance
                // Record the failed transaction
                createTransaction(accountId, null, amount, "withdraw", "failed");

                throw new RuntimeException("Insufficient funds");
            }
        } else {
            throw new RuntimeException("Account not found");
        }
    }

    // Deposit funds into an account
    public void deposit(String accountId, Double amount) {
        Optional<Account> accountOptional = accountRepository.findByAccountId(accountId);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            // Update the balance
            account.setBalance(account.getBalance().doubleValue() + amount);
            // Record the transaction
            createTransaction(accountId, null, amount, "deposit", "success");
            accountRepository.save(account);
        }
    }

    // Transfer funds between accounts
    public void transfer(String fromAccountId, String toAccountId, Double amount) {
        Optional<Account> fromAccountOptional = accountRepository.findByAccountId(fromAccountId);
        Optional<Account> toAccountOptional = accountRepository.findByAccountId(toAccountId);

        if (fromAccountOptional.isPresent() && toAccountOptional.isPresent()) {
            Account fromAccount = fromAccountOptional.get();
            Account toAccount = toAccountOptional.get();

            if (fromAccount.getBalance().doubleValue() >= amount) {
                // Deduct from sender account
                fromAccount.setBalance(fromAccount.getBalance().doubleValue() - amount);
                // Add to recipient account
                toAccount.setBalance(toAccount.getBalance().doubleValue() + amount);

                // Save updated accounts
                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);

                // Record the transactions for both sender and receiver
                createTransaction(fromAccountId, toAccountId, amount, "transfer", "success");
                createTransaction(toAccountId, fromAccountId, amount, "transfer", "success");

            } else {
                // Insufficient balance
                createTransaction(fromAccountId, toAccountId, amount, "transfer", "failed");
                throw new RuntimeException("Insufficient funds");
            }
        }
    }

    // Helper method to create a transaction
    private void createTransaction(String fromAccount, String toAccount, Double amount, String type, String status) {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);  // Null if not a transfer
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus(status);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);  // Save the transaction to the DB
    }

    // Get all transactions for a specific account
    public List<Transaction> getTransactionsByAccount(String accountId) {
        return transactionRepository.findByFromAccount(accountId);
    }
}
