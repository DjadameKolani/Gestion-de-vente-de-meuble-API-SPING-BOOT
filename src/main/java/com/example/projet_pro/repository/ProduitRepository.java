package com.example.projet_pro.repository;

import com.example.projet_pro.entity.Produit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepository extends MongoRepository<Produit, String> {
}