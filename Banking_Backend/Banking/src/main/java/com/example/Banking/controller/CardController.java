
package com.example.Banking.controller;

import com.example.Banking.dto.CardRequest;
import com.example.Banking.dto.ResponseMessage;
import com.example.Banking.entity.Card;
import com.example.Banking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    // Apply for a card
    @PostMapping("/apply")
    public ResponseEntity<?> applyCard(@RequestBody CardRequest request) {
        try {
            Card card = cardService.applyCard(request.getUserId(), request.getCardType(), request.getCardLimit());
            return ResponseEntity.status(HttpStatus.CREATED).body(card);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Failed to apply for card: " + e.getMessage()));
        }
    }

    // Block a card
    @PutMapping("/block/{cardId}")
    public ResponseEntity<?> blockCard(@PathVariable String cardId) {
        try {
            Card card = cardService.blockCard(cardId);
            return ResponseEntity.ok(new ResponseMessage("success", "Card blocked."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Failed to block card: " + e.getMessage()));
        }
    }

    // Update card limit
    @PutMapping("/update-limit/{cardId}")
    public ResponseEntity<?> updateCardLimit(
            @PathVariable String cardId,
            @RequestBody CardRequest updateLimitRequest) {
        try {
            Card card = cardService.updateCardLimit(cardId, updateLimitRequest.getNewLimit());
            return ResponseEntity.ok(new ResponseMessage("success", "Card limit updated."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Failed to update card limit: " + e.getMessage()));
        }
    }


    // Admin - Get all cards
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllCards() {
        try {
            List<Card> cards = cardService.getAllCards();
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Error fetching cards: " + e.getMessage()));
        }
    }

    // User - Get all cards by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCardsByUserId(@PathVariable String userId) {
        try {
            List<Card> cards = cardService.getCardsByUserId(userId);
            return ResponseEntity.ok(cards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("error", "Error fetching cards: " + e.getMessage()));
        }
    }
}
