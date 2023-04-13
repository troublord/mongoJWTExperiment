package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.models.User;

public interface UserRepo extends MongoRepository<User,String>{
    Optional<User>findUserByEmail(String email);
}