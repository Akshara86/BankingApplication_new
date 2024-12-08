
package com.example.Banking.service;

import com.example.Banking.entity.Card;
import com.example.Banking.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    // Apply for a card
    public Card applyCard(String userId, Card.CardType cardType, Double cardLimit) {
        Card card = new Card();
        card.setCardId(UUID.randomUUID().toString());
        card.setUserId(userId);
        card.setCardType(cardType);
        card.setCardNumber(generateMaskedCardNumber());
        card.setExpiryDate(LocalDateTime.now().plusYears(5));
        card.setCvvHash(encryptCVV(generateCVV()));
        card.setStatus(Card.CardStatus.ACTIVE);
        card.setCardLimit(cardLimit);

        return cardRepository.save(card);
    }

    // Block a card
    public Card blockCard(String cardId) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isPresent()) {
            Card card = cardOptional.get();
            card.setStatus(Card.CardStatus.BLOCKED);
            return cardRepository.save(card);
        } else {
            throw new RuntimeException("Card not found.");
        }
    }

    // Update card limit
    public Card updateCardLimit(String cardId, Double newLimit) {
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isPresent()) {
            Card card = cardOptional.get();
            card.setCardLimit(newLimit);
            return cardRepository.save(card);
        } else {
            throw new RuntimeException("Card not found.");
        }
    }

    // Admin fetch all cards
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    // Get cards by user ID
    public List<Card> getCardsByUserId(String userId) {
        return cardRepository.findByUserId(userId);
    }

    // Utility methods
    private String generateMaskedCardNumber() {
        return "XXXX-XXXX-XXXX-" + (int) (Math.random() * 10000);
    }

    private String generateCVV() {
        return String.format("%03d", (int) (Math.random() * 1000));
    }

    private String encryptCVV(String cvv) {
        return Integer.toHexString(cvv.hashCode()); // Simple hash for demonstration
    }
}
