package com.example.projet_pro.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")  // ← remplace @Entity
@Data
public class User {

    @Id
    private String id;

    @Indexed(unique = true)// <- qaund ses unique on l'ajoute au repository
    private String username;

    private String prenom;

    @Indexed(unique = true)  // ← email aussi unique
    private String email;    // ← ajouté

    private String password;

    private String confirmPassword; // ← ajouté

    private String role = "ROLE_USER";  // ← rôle par défaut
}