package com.example.projet_pro.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "commandes")
@Data
public class Commande {

    @Id
    private String id;

    private String username;     // ← qui a commandé

    private List<ProduitCommande> produits;  // ← liste des produits

    private Double total;

    private String statut = "EN_ATTENTE";  // EN_ATTENTE, PAYÉ, LIVRÉ

    private Date dateCommande = new Date();
}