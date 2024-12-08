
package com.example.Banking.repository;

import com.example.Banking.entity.Card;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CardRepository extends MongoRepository<Card, String> {
    List<Card> findByUserId(String userId);
}
