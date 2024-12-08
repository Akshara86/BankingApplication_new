package com.example.Banking.repository;


import com.example.Banking.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

     User findByEmail(String email);
     long countByIsActive(boolean isActive);
}