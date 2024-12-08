package com.example.Banking.controller;

import com.example.Banking.dto.ResponseMessage;
import com.example.Banking.dto.TransactionRequest;
import com.example.Banking.dto.TransferRequest;
import com.example.Banking.entity.Account;
import com.example.Banking.entity.Transaction;
import com.example.Banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Endpoint to open a new account
    @PostMapping("/open")
    public ResponseEntity<?> openAccount(@RequestBody Account account) {
        try {
            // Create the account
            Account createdAccount = accountService.openAccount(account.getUserId(),
                    account.getAccountType(),
                    account.getBalance(),
                    account.getAadharNumber(),
                    account.getPan(),
                    account.getGender());

            // Return the created account details with status CREATED
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdAccount);
        } catch (IllegalArgumentException e) {
            // If account already exists, return an appropriate error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", e.getMessage()));
        } catch (Exception e) {
            // General error handling
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("error", "An unexpected error occurred: " + e.getMessage()));
        }
    }

//     Endpoint to update account balance
    @PutMapping("/update/{accountId}")
    public ResponseEntity<ResponseMessage> updateAccount(@PathVariable String accountId, @RequestParam Number newBalance) {
        try {
            Account updatedAccount = accountService.updateAccount(accountId, newBalance);
            return ResponseEntity.ok(new ResponseMessage("success", "Account balance updated successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Failed to update account: " + e.getMessage()));
        }
    }

    // Endpoint to close an account (if balance is zero)
    @DeleteMapping("/close/{accountId}")
    public ResponseEntity<ResponseMessage> closeAccount(@PathVariable String accountId) {
        try {
            boolean isClosed = accountService.closeAccount(accountId);
            if (isClosed) {
                return ResponseEntity.ok(new ResponseMessage("success", "Account closed and deleted successfully."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseMessage("error", "Failed to close account. Balance must be zero."));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("error", e.getMessage()));
        }
    }


    // Endpoint to retrieve account by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getAccountByUserId(@PathVariable String userId) {
        try {
            List<Account> accounts = accountService.getAccountByUserId(userId);
            if (accounts.isEmpty()) {
                ResponseMessage errorResponse = new ResponseMessage("error", "No accounts found for userId: " + userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            return ResponseEntity.ok(accounts);  // Return the list of accounts if found
        } catch (Exception e) {
            ResponseMessage errorResponse = new ResponseMessage("error", "Error fetching accounts for userId: " + userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    @PutMapping("/withdraw/{accountId}")
    public ResponseEntity<ResponseMessage> withdraw(@PathVariable String accountId, @RequestBody TransactionRequest request) {
        try {
            accountService.withdraw(accountId, request.getAmount());
            return ResponseEntity.ok(new ResponseMessage("success", "Withdrawal successful!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Failed to withdraw: " + e.getMessage()));
        }
    }

    // Endpoint to deposit funds into an account
    @PutMapping("/deposit/{accountId}")
    public ResponseEntity<ResponseMessage> deposit(@PathVariable String accountId, @RequestBody TransactionRequest request) {
        try {
            accountService.deposit(accountId, request.getAmount());
            return ResponseEntity.ok(new ResponseMessage("success", "Deposit successful!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Failed to deposit: " + e.getMessage()));
        }
    }

    // Endpoint to transfer funds between accounts
    @PutMapping("/transfer")
    public ResponseEntity<ResponseMessage> transfer(@RequestBody TransferRequest request) {
        try {
            accountService.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount());
            return ResponseEntity.ok(new ResponseMessage("success", "Transfer successful!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Failed to transfer: " + e.getMessage()));
        }
    }

    // Endpoint to retrieve all transactions for a specific account
    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<Object> getTransactions(@PathVariable String accountId) {
        try {
            List<Transaction> transactions = accountService.getTransactionsByAccount(accountId);
            if (transactions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseMessage("error", "No transactions found for account: " + accountId));
            }
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Failed to retrieve transactions: " + e.getMessage()));
        }
    }
}
