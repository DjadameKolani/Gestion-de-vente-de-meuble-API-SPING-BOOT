package com.example.projet_pro.repository;

import com.example.projet_pro.entity.Commande;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends MongoRepository<Commande, String> {
    List<Commande> findByUsername(String username);
}