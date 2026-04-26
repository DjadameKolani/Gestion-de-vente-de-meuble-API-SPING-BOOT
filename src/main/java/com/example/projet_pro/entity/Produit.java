
package com.example.projet_pro.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "produits")
@Data
public class Produit {

    @Id
    private String id;

    private String nom;

    private String description;

    private Double prix;

    private Integer quantite;

    private String image;  // ← URL de l'image
}